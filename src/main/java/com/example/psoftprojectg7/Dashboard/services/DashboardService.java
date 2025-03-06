package com.example.psoftprojectg7.Dashboard.services;

import com.example.psoftprojectg7.Dashboard.api.CashFlowDTO;
import com.example.psoftprojectg7.Dashboard.api.RevenueDTO;
import org.springframework.stereotype.Service;

@Service
public interface DashboardService {
    Double calculateCashFlowForMonth(String planName, int month, String timeline);
    CashFlowDTO calculateCashFlow(String timeline, int months, String plan);
    RevenueDTO calculateRevenue(String plan);

    /**
     * Gets new subscribers

     * @param month
     * @return
     */
    int countNewSubscriptionsByMonth(int month);

    /**
     * Gets cancellations
     * @param month
     * @return
     */
    int countCancelledSubscriptionsByMonth(int month);

    /**
     * Switches plan
     * @param resource
     * @return
     */
}