package com.example.psoftprojectg7.SubscriptionManagement.services;

import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.SubscriptionManagement.api.*;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Service
public interface SubscriptionService {
    /**
     * Creates a new subscription.
     *
     * @param resource
     * @return
     */
    Subscription create(CreateSubscriptionRequest resource);
    Iterable<Subscription> findAll();
    /**
     * Finds a subscription by id.
     *
     * @param subscriptionId
     * @return
     */
    Optional<Subscription> findOne(Long subscriptionId);

    /**
     * Migrates subscribers from one plan to another.
     *
     * @param planName
     * @param resource
     * @return
     */
    ArrayList<Subscription> migrate(String planName, MigrateSubsPlanRequest resource);
    /**
     * Cancels a subscription.
     *
     * @param Id
     * @return
     */
    Subscription cancel(Long Id);

    /**
     * Reactivates a subscription.
     *
     * @param Id
     * @return
     */
    Subscription reActivate(Long Id);
    //List<Integer> findByMonth(String month, Subscription subscription);



    Subscription switchPlan(SwitchPlanRequest resource, Long userId);

    /**
     * Renews annual subscription
     * @param Id
     * @return
     */

    Subscription renewAnnualSubscription(Long Id);

    Plan getPlanDetails(Long id);
    String getJoke(Long Id) throws IOException;

    //String getWeather(Long Id, WeatherForecastRequest resource) throws IOException;

}
