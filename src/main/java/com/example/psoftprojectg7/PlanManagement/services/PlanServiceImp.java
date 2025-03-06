package com.example.psoftprojectg7.PlanManagement.services;


import com.example.psoftprojectg7.PlanManagement.api.CreatePlanRequest;
import com.example.psoftprojectg7.PlanManagement.api.EditPlanRequest;
import com.example.psoftprojectg7.PlanManagement.api.EditPriceRequest;
import com.example.psoftprojectg7.PlanManagement.api.PlanMapper;
import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.PlanManagement.model.PriceHistory;
import com.example.psoftprojectg7.PlanManagement.repositories.PlanRepository;
import com.example.psoftprojectg7.PlanManagement.repositories.PriceHistoryRepository;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import com.example.psoftprojectg7.SubscriptionManagement.repositories.SubscriptionRepository;
import com.example.psoftprojectg7.UserManagement.model.User;
import com.example.psoftprojectg7.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanServiceImp implements PlanService {


    private PlanRepository repository;
    private EditPlanMapper planEditMapper;
    private PlanMapper mapper;

    private PriceHistoryRepository priceHistoryRepository;
    private SubscriptionRepository subscriptionRepository;


    @Autowired
    public void PlanServiceImpl(PlanRepository planRepository, EditPlanMapper planEditMapper, PlanMapper mapper, PriceHistoryRepository priceHistoryRepository, SubscriptionRepository subscriptionRepository) {
        this.repository = planRepository;
        this.planEditMapper = planEditMapper;
        this.mapper = mapper;
        this.priceHistoryRepository = priceHistoryRepository;
        this.subscriptionRepository = subscriptionRepository;

    }


    @Override
    public Iterable<Plan> findAll() {
        return repository.findAll();
    }


    @Override
    @Transactional
    public Plan createPlan(final CreatePlanRequest resource) {
        if (repository.existsByPlanName(resource.getPlanName())) {
            throw new IllegalArgumentException("A plan with the provided name already exists.");
        }
        final Plan plan = planEditMapper.create(resource);

        return repository.save(plan);
    }


    @Override
    public Plan update(final String planName, final EditPlanRequest resource) {
        final var plan = repository.findByPlanName(planName);

        if (plan == null) {
            throw new IllegalArgumentException("This plan doesn't exist!");
        }
        // apply update
        plan.update(resource.getNumberOfMinutes(), resource.getPlanDescription(), resource.getMaxUsers(), resource.getMusicCollections(), resource.getMusicSuggestions(), resource.isActive(), resource.isPromoted(), resource.isArchived());
        return repository.save(plan);
    }


    public Plan cancel(String planName) {
        final var plan = repository.findByPlanName(planName);
        if (plan == null) {
            throw new IllegalArgumentException("This plan doesn't exist!");
        } else {
            if (plan.isActive())
            {
                throw new IllegalArgumentException("This plan is already deactived!");
            }
            plan.setActive(false);
            return repository.save(plan);
        }
    }

    @Override
    public Plan updatePrice(String planName, EditPriceRequest resource, String userName) {
        final var plan = repository.findByPlanName(planName);
        PriceHistory priceHistory = new PriceHistory();
        double test = plan.getMonthlyFee();
        priceHistory.setPreviousMonthlyFee(plan.getMonthlyFee());
        priceHistory.setPreviousAnnualFee(plan.getAnnualFee());
        if (plan == null) {
            throw new IllegalArgumentException("This plan doesn't exist!");
        } else {

            priceHistory.setCurrentMonthlyFee(resource.getMonthlyFee());
            priceHistory.setCurrentAnnualFee(resource.getAnnualFee());
            priceHistory.setLastPriceChangeDate(LocalDate.now());
            priceHistory.setPlanName(plan.getPlanName());
            priceHistory.setUsername(userName);
            priceHistoryRepository.save(priceHistory);
            plan.applyPrice(resource.getMonthlyFee(), resource.getAnnualFee());

            return repository.save(plan);
        }
    }

    public Plan cease(String planName) {
        final var plan = repository.findByPlanName(planName);
        final var sub = subscriptionRepository.findByPlanName(planName);
        // check if the plan isn´t active
        if (plan.isActive() && sub != null) {
            throw new IllegalArgumentException("The plan is active or has subscriptions associated");
        } else {
            plan.setArchived(true);

            return repository.save(plan);
        }
    }

    @Override
    public Plan promote(String planName) {
        final var plan = repository.findByPlanName(planName);
        if (plan == null) {
            throw new IllegalArgumentException("This plan doesn't exist!");
        } else {
            if (plan.isPromoted())
            {
                throw new IllegalArgumentException("This plan is already promoted!");
            }
            plan.setPromoted(true);
            return repository.save(plan);
        }
    }

    @Override
    public Iterable<PriceHistory> getPriceHistory(String planName) {
        List<PriceHistory> pricesList = new ArrayList<>();
        final var plan = repository.findByPlanName(planName);
        if (plan == null) {
            throw new IllegalArgumentException("This plan doesn't exist!");
        }
        else{
            pricesList = priceHistoryRepository.findByPlanNameOrderByLastPriceChangeDateDesc(planName);
            return pricesList;
        }
    }
}
