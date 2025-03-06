package com.example.psoftprojectg7.SubscriptionManagement.repositories;

import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    @Override
    Optional<Subscription> findById(Long id);
    Subscription findBySubscriptionId(Long id);

    Subscription findByUserId(Long userId);
    ArrayList<Subscription> findByPlanName(String planName);

    @Query("SELECT s FROM Subscription s WHERE planName (s.planName) = :planName")
    ArrayList<Subscription> findSubsWithSpecificPlan(@Param ("planName") String planName);
    @Query("SELECT COUNT(s) FROM Subscription s WHERE MONTH (s.subscriptionDate) = :month")
    int countNewSubscriptionsByMonth(@Param ("month") int month); //conta o numero de subscrições de um mês
    @Query("SELECT COUNT(s) FROM Subscription s WHERE MONTH (s.cancellationDate) = :month")
    int countCancelledSubscriptionsByMonth(@Param ("month") int month); //conta o numero de cancelamentos de um mês
}

