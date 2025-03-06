package com.example.psoftprojectg7.model;

import com.example.psoftprojectg7.SubscriptionManagement.api.EditSubscriptionRequest;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import org.hibernate.StaleObjectStateException;
import org.junit.Test;

import java.time.LocalDate;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertThrows;

public class SubscriptionTest {
    @Test
    public void ensurePlanNameIsSet() {
        final var subject = new Subscription(1L);
        assertEquals("Gold", subject.getPlanName());
    }

    @Test
    public void ensureSubscriptionDateIsSet() {
        final var subject = new Subscription(1L);
        subject.setSubscriptionDate(LocalDate.now());
        assertEquals(LocalDate.now(), subject.getSubscriptionDate());
    }

    @Test
    public void ensureCancellationDateIsSet() {
        final var subject = new Subscription(1L);
        subject.setCancellationDate();
        assertEquals(LocalDate.now(), subject.getCancellationDate());
    }

    @Test
    public void ensureLastRenovationDateIsSet() {
        final var subject = new Subscription(1L);
        subject.setLastRenovationDate(LocalDate.now());
        assertEquals(LocalDate.now(), subject.getLastRenovationDate());
    }
    @Test
    public void ensurePatchNameIsIgnored() {
        final var patch = new EditSubscriptionRequest("Gold", "Monthly");
        final var subject = new Subscription("Gold");

        subject.applyPatch(patch.getPlanName(), patch.getPaymentFrequency());

        assertEquals("Gold", subject.getPlanName());
    }

    @Test
    public void ensurePatchPaymentFrequency() {
        final var patch = new EditSubscriptionRequest("Gold", "Monthly");

        final var subject = new Subscription(1L);
        subject.setPaymentFrequency("Monthly");

        subject.applyPatch(patch.getPlanName(), patch.getPaymentFrequency());

        assertEquals("Monthly", subject.getPaymentFrequency());
    }

    @Test
    public void ensurePatchPlanName() {
        final var patch = new EditSubscriptionRequest("Gold", "Monthly");

        final var subject = new Subscription(1L);
        subject.setPlanName("Gold");

        subject.applyPatch(patch.getPlanName(), patch.getPaymentFrequency());

        assertEquals("Gold", subject.getPlanName());
    }
}
