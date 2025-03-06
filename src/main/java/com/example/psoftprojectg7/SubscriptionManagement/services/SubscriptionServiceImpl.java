package com.example.psoftprojectg7.SubscriptionManagement.services;

import com.example.psoftprojectg7.DeviceManagement.model.Device;
import com.example.psoftprojectg7.DeviceManagement.repositories.DeviceRepository;
import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.PlanManagement.repositories.PlanRepository;
import com.example.psoftprojectg7.SubscriptionManagement.api.*;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import com.example.psoftprojectg7.SubscriptionManagement.model.Utils;
import com.example.psoftprojectg7.SubscriptionManagement.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService{

    private SubscriptionRepository subscriptionRepository;
    private EditSubscriptionMapper editSubscriptionMapper;

    private PlanRepository planRepository;
    private SubscriptionMapper subscriptionMapper;
    private DeviceRepository deviceRepository;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, EditSubscriptionMapper editSubscriptionMapper, PlanRepository planRepository, DeviceRepository deviceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.editSubscriptionMapper = editSubscriptionMapper;
        this.planRepository = planRepository;
        this.deviceRepository = deviceRepository;
    }

    public Iterable<Subscription> findAll() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Optional<Subscription> findOne(final Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId);
    }
    @Override
    @Transactional
    public Subscription create(CreateSubscriptionRequest resource){
        final var subscription = editSubscriptionMapper.create(resource);
        final Plan plan = planRepository.findByPlanName(resource.getPlanName());
        if (Objects.equals(resource.getPlanName(), "") || Objects.equals(resource.getPaymentFrequency(), "")){
            throw new IllegalArgumentException("A plan Name and a payment Frequency must be provided!");
        }
        if (plan == null){
            throw new IllegalArgumentException("The plan you wish to subscribe does not exist");
        }else {
            if (!plan.isActive()) {
                throw new IllegalArgumentException("The plan you wish to subscribe isn't currently active!");
            } else {
                LocalDate date1 = LocalDate.now();
                subscription.setSubscriptionDate(date1);
                return subscriptionRepository.save(subscription);
            }
        }
    }

    @Override
    public Subscription switchPlan(SwitchPlanRequest resource, Long userId){
        final var subscription = subscriptionRepository.findByUserId(userId);
        final var plan = planRepository.findByPlanName(resource.getNewPlan());
        if (plan == null) {
            throw new IllegalArgumentException("The plan you wish to switch to doesn't exist!");
        } else {
            if (Objects.equals(resource.getNewPlan(), subscription.getPlanName())) {
                throw new IllegalArgumentException("You are already subscribed to this plan!");
            } else {
                if(subscription.getCurrentDevices() > plan.getMaxUsers()){
                    throw new IllegalArgumentException("The plan you wish to switch to allows you to have less devices." +
                            " If you wish to switch to this plan, please eliminate some devices from your subscription first!");
                } else{
                    subscription.setPlanName(resource.getNewPlan());
                    return subscriptionRepository.save(subscription);
                }
            }
        }
    }

    @Override
    public ArrayList<Subscription> migrate(String planName, MigrateSubsPlanRequest resource) {
        final var plan = planRepository.findByPlanName(planName);
        if (plan == null) {
            throw new IllegalArgumentException("The plan doesn´t exist");
        }
        final var maybePlan = planRepository.findByPlanName(resource.getNewPlanName());
            if (maybePlan == null) {
                throw new IllegalArgumentException("Cannot find plan to migrate to");
            }
        if (planName.equals(resource.getNewPlanName())){
            throw new IllegalArgumentException("The plan names inserted mustn't match!");
        }
        ArrayList<Subscription> subscriptions = new ArrayList<>();
        subscriptions = subscriptionRepository.findByPlanName(planName);

        if (subscriptions.isEmpty()){
            throw new IllegalArgumentException("There are no subscriptions to the plan inserted, therefore they can't migrate to a new plan!");
        }

        for (Subscription subscription : subscriptions) {
            Iterable<Device> devices = deviceRepository.findByUserId(subscription.getUserId());
            deviceRepository.deleteAll(devices);

            subscription.setPlanName(resource.getNewPlanName());
            subscription.setCurrentDevices(0);
            subscriptionRepository.save(subscription);
        }

        return subscriptions;
    }

    public Subscription renewAnnualSubscription(Long Id){
        final var subscription = subscriptionRepository.findByUserId(Id);
        LocalDate date = LocalDate.now();
        if (subscription == null){
            throw new IllegalArgumentException("The user doesn't have a subscription yet!");
        } else {
            if (Objects.equals(subscription.getPaymentFrequency(), "Monthly")){
                throw new IllegalArgumentException("You're subscription isn't registered with an annual paymentFrequency!");
            }  else {
                if (Objects.equals(subscription.getPaymentFrequency(), "Annually") && date.getMonth()==subscription.getSubscriptionDate().getMonth() && date.getDayOfMonth()==subscription.getSubscriptionDate().getDayOfMonth() && (date.getYear()==subscription.getSubscriptionDate().getYear()+1)){
                    subscription.setCancellationDate();
                    throw new IllegalArgumentException("You can no longer renew your subscription! Please try to reactivate it first!");
                }
                subscription.setLastRenovationDate(LocalDate.now());
                return subscriptionRepository.save(subscription);
            }
        }
    }
    public Subscription cancel(Long Id) {
        final var sub = subscriptionRepository.findByUserId(Id);
            sub.setCancellationDate();
            sub.setActive(false);

            return subscriptionRepository.save(sub);
    }

    public Subscription reActivate(Long Id) {
        final var sub = subscriptionRepository.findByUserId(Id);
        if (sub.calculateDate(sub.getSubscriptionDate(), sub.getCancellationDate()) <= 30) {
            if (sub.isActive()){
                throw new IllegalArgumentException("The subscription you wish to activate is already active");
            }else{
                sub.setActive(true);
                sub.removeCancellationDate();
                return subscriptionRepository.save(sub);
            }
        } else {
            throw new IllegalArgumentException("The period of 30 days to reactivate a cancelled subscription has expired!");
        }
    }

    public Plan getPlanDetails(Long id){
        final var subscription = subscriptionRepository.findByUserId(id);
        return planRepository.findByPlanName(subscription.getPlanName());
    }

    @Override
    public String getJoke(Long Id) throws IOException {
        final var subscription = subscriptionRepository.findByUserId(Id);
        String joke = Utils.sendGETJoke();
        subscription.setJoke(joke);
        subscriptionRepository.save(subscription);
        return joke;
    }

  /*@Override
    public String getWeather(Long Id, WeatherForecastRequest resource) throws IOException {
        final var subscription = subscriptionRepository.findByUserId(Id);
        String weather = Utils.sendGETWeather();
        subscription.setJoke(weather);
        subscriptionRepository.save(subscription);
        return weather;
    }*/
}

