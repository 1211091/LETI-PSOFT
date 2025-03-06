package com.example.psoftprojectg7.Dashboard.services;

import com.example.psoftprojectg7.Dashboard.api.RevenueMapper;
import com.example.psoftprojectg7.Dashboard.model.RevenueObj;
import com.example.psoftprojectg7.Dashboard.api.CashFlowDTO;
import com.example.psoftprojectg7.Dashboard.api.CashFlowMapper;
import com.example.psoftprojectg7.Dashboard.api.RevenueDTO;
import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.PlanManagement.repositories.PlanRepository;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import com.example.psoftprojectg7.SubscriptionManagement.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService{
    private SubscriptionRepository subscriptionRepository;
    private PlanRepository planRepository;
    private CashFlowMapper cashFlowMapper;
    private RevenueMapper revenueMapper;


    @Autowired
    public DashboardServiceImpl(SubscriptionRepository subscriptionRepository, PlanRepository planRepository, CashFlowMapper cashFlowMapper, RevenueMapper revenueMapper ){
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.cashFlowMapper = cashFlowMapper;
        this.revenueMapper = revenueMapper;
    }

    public Double calculateCashFlowForMonth(String planName, int month, String timeline){

        int activeMonthlySubs = 0;
        int activeAnnualSubs = 0;

        ArrayList<Subscription> subscriptions = subscriptionRepository.findByPlanName(planName);

        if(timeline.equals("future")) {
            for (Subscription subscription : subscriptions) {
                if (subscription.isActive() ||
                        // Se o cancelamento tiver ocorrido no mês a analisar o pagamento deverá ser contabilizado
                        (!subscription.isActive() && subscription.getCancellationDate().getMonthValue() == month)) {
                    if (Objects.equals(subscription.getPaymentFrequency(), "Monthly"))
                        activeMonthlySubs++;
                    else if (Objects.equals(subscription.getPaymentFrequency(), "Annualy")) {
                        // subscrição anual só deverá ser contabilizada se o seu vencimento não ultrapassar a data a analisar
                        if (subscription.getSubscriptionDate().plusYears(1).isAfter(LocalDate.now().plusMonths(month)))
                            activeAnnualSubs++;
                    }
                }
            }
        } else { //timeline.equals("past")
            for (Subscription subscription : subscriptions) {
                    if (Objects.equals(subscription.getPaymentFrequency(), "Monthly"))
                        // Apenas as subscrições realizadas anteriormente à data a analisar devem ser contabilizadas
                        if (subscription.getSubscriptionDate().isBefore(LocalDate.now().minusMonths(month)) && (subscription.getCancellationDate() == null ||
                                // caso o cancelamento tenha ocorrido após a data a analisar o pagamento deverá ser considerado
                                subscription.getCancellationDate() != null && subscription.getCancellationDate().isAfter(LocalDate.now().minusMonths(month))))
                            activeMonthlySubs++;
                    else if (Objects.equals(subscription.getPaymentFrequency(), "Annualy")) {
                        // subscrição anual só deverá ser contabilizada se o seu vencimento ultrapassar a data a analisar
                        if (subscription.getSubscriptionDate().plusYears(1).isBefore(LocalDate.now().minusMonths(month)))
                            activeAnnualSubs++;
                    }
            }
        }

        Plan plan = planRepository.findByPlanName(planName);
        return (activeMonthlySubs * plan.getMonthlyFee()) + (activeAnnualSubs * (plan.getAnnualFee()/12));

    }

    @Override
    public CashFlowDTO calculateCashFlow(String timeline, int months, String planFilter) {
        Iterable<Plan> plans = planRepository.findAll();

        List<String> columns = new ArrayList<>();
        List<List<Object>> rows = new ArrayList<>();

        // Populate columns
        columns.add("Plan");
        for (int i = 1; i <= months; i++) {
            if(timeline.equals("future"))
                columns.add(LocalDate.now().plusMonths(i).getMonth().name());
            else // timeline.equals("past")
                columns.add(LocalDate.now().minusMonths(i).getMonth().name());
        }

        // Calculate monthly cashflow by plan for each month
        if (planFilter != null && !planFilter.isEmpty()) {

            // If filter is present consider only that plan
            boolean planFound = false;
            for (Plan plan : plans) {
                if (plan.getPlanName().equals(planFilter)) {    // Check if plan is valid
                    planFound = true;

                    List<Object> row = new ArrayList<>();
                    row.add(plan.getPlanName());    // add plan name
                    for (int i = 1; i <= months; i++) {
                        double cashflowForMonth = calculateCashFlowForMonth(plan.getPlanName(), i, timeline);
                        row.add(cashflowForMonth);
                    }
                    rows.add(row);
                    break;
                }
            }

            if (!planFound)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Plan not found!");

        } else // Use all plans
        {
            for (Plan plan : plans) {

                List<Object> row = new ArrayList<>();
                row.add(plan.getPlanName());    // add plan name

                for (int i = 1; i <= months; i++) {
                    double cashflowForMonth = calculateCashFlowForMonth(plan.getPlanName(), i, timeline);
                    row.add(cashflowForMonth);
                }
                rows.add(row);
            }
        }

        //Calculate total
        List<Object> totalCashflow = new ArrayList<>();
        totalCashflow.add("Total");
        for (int col = 1; col <= months; col++) {
            double sum = 0;
            for (int row = 0; row < rows.size(); row++)
                sum += (double) rows.get(row).get(col);
            totalCashflow.add(sum);
        }
        rows.add(totalCashflow);

        return cashFlowMapper.toCashFlowDTO(columns, rows);
    }

    @Override
    public RevenueDTO calculateRevenue(String planFilter) {

        Iterable<Plan> plans;

        // Check if planFilter is provided and filter the plans accordingly
        if (planFilter != null && !planFilter.isEmpty()) {
            Plan filteredPlan = planRepository.findByPlanName(planFilter);
            plans = Collections.singletonList(filteredPlan);
        } else {
            plans = planRepository.findAll();
        }

        List<String> columns = new ArrayList<>();
        List<RevenueObj> rows = new ArrayList<>();

        //Fill columns
        columns.add("Plan");
        columns.add("Metric");
        columns.add("Total");

        // Calculate revenue for each plan
        for (Plan plan : plans) {

            String planName = plan.getPlanName();

            ArrayList<Subscription> subscriptions = subscriptionRepository.findByPlanName(plan.getPlanName());
            // calculate active subscriptions
            int activeSubs = 0;

            for (Subscription subscription : subscriptions) {
                if (subscription.isActive())
                    activeSubs++;
            }

            // calculate new subscriptions
            int newSubs = 0;

            for (Subscription subscription : subscriptions) {
                if (subscription.isActive() && subscription.getSubscriptionDate().isAfter(LocalDate.now().minusDays(30)))
                    newSubs++;
            }

            // calculate cancelled subscriptions
            int cancelledSubs = 0;

            for (Subscription subscription : subscriptions) {
                if (!subscription.isActive() && subscription.getCancellationDate().isAfter(LocalDate.now().minusDays(30)))
                    cancelledSubs++;
            }

            // calculate churn rate (%)
            double churnRate = 0;
            if(cancelledSubs != 0 && activeSubs != 0)
                churnRate = (double) cancelledSubs / activeSubs * 100;

            // calculate revenue generated so far
            double revenue = 0;
            for (Subscription subscription : subscriptions) {
                // Consider only subscriptions from the current year
                if(subscription.getSubscriptionDate().isAfter(LocalDate.now().withDayOfYear(1))) {
                    if (Objects.equals(subscription.getPaymentFrequency(), "Monthly")) {
                        revenue += planRepository.findByPlanName(subscription.getPlanName()).getMonthlyFee();
                    }
                    else if (Objects.equals(subscription.getPaymentFrequency(), "Annualy")) {
                        revenue += planRepository.findByPlanName(subscription.getPlanName()).getAnnualFee();
                    }
                }
            }

            RevenueObj.Metric metrics = new RevenueObj.Metric(activeSubs, newSubs, cancelledSubs, churnRate, revenue);
            RevenueObj revenueObj = new RevenueObj(planName, metrics);
            rows.add(revenueObj);
        }

        // Calculate Total
        int totalActiveSubs = 0;
        int totalNewSubs = 0;
        int totalCancelledSubs = 0;
        double totalChurnRate = 0;
        double totalRevenue = 0;


        for (int i = 0; i < rows.size(); i++) {
            totalActiveSubs += rows.get(i).getMetric().getActiveSubscriptions();
            totalNewSubs += rows.get(i).getMetric().getNewSubscriptions();
            totalCancelledSubs += rows.get(i).getMetric().getCancelledSubscriptions();
            totalChurnRate += rows.get(i).getMetric().getChurnRate();
            totalRevenue += rows.get(i).getMetric().getRevenue();
        }


        RevenueObj.Metric metrics = new RevenueObj.Metric(totalActiveSubs, totalNewSubs, totalCancelledSubs, totalChurnRate, totalRevenue);
        RevenueObj revenueObj = new RevenueObj("TOTAL", metrics);
        rows.add(revenueObj);

        return revenueMapper.toRevenueDTO(columns, rows);
    }

    public int countNewSubscriptionsByMonth(int month){
        int count = subscriptionRepository.countNewSubscriptionsByMonth(month);
        return count;
    }

    public int countCancelledSubscriptionsByMonth(int month){
        int count = subscriptionRepository.countCancelledSubscriptionsByMonth(month);
        return count;
    }

}
