package com.example.psoftprojectg7.SubscriptionManagement.model;

import com.example.psoftprojectg7.DeviceManagement.model.Device;
import com.example.psoftprojectg7.UserManagement.model.User;
import org.hibernate.StaleObjectStateException;

import javax.persistence.*;

import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Table (name = "subscriptions")
@Entity
public class Subscription{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subscriptionId;
    @NotBlank
    @NotNull(message = "A plan name must be provided. I.e: 'Gold'")
    private String planName;
    @Transient
    @OneToMany
    private List<Device> devices = new ArrayList<>();;
    @Version
    private long version;
    private LocalDate subscriptionDate;
    private LocalDate cancellationDate;
    private LocalDate lastRenovationDate;
    private int currentDevices = 0;
    @NotNull
    @NotBlank(message = "A payment frequency must be provided. I.e: 'Monthly'")
    private String paymentFrequency;
    private boolean isActive = true;
    private boolean isSubscription = true;
    private Long userId;
    private String joke;

    public Subscription(String planName, List<Device> devices, LocalDate subscriptionDate, LocalDate cancellationDate, LocalDate lastRenovationDate, int currentDevices, String paymentFrequency, boolean isActive, boolean isSubscription, Long userId, String joke) {
        setPlanName(planName);
        setDevices(devices);
        setSubscriptionDate(subscriptionDate);
        this.cancellationDate = cancellationDate;
        setLastRenovationDate(lastRenovationDate);
        setCurrentDevices(currentDevices);
        setPaymentFrequency(paymentFrequency);
        setActive(isActive);
        setSubscription(isSubscription);
        setUserId(userId);
        setJoke(joke);
    }

    public Subscription(final String planName, final Long userId, final String paymentFrequency) {
        setPlanName(planName);
        setUserId(userId);
        setPaymentFrequency(paymentFrequency);
    }

    public Subscription(){}

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }


    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public LocalDate getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(LocalDate subscriptionDate) {
        this.subscriptionDate = LocalDate.now();
    }

    public LocalDate getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate() {
       this.cancellationDate = LocalDate.now();
    }

    public void removeCancellationDate(){this.cancellationDate = null;}
    public LocalDate getLastRenovationDate() {
        return lastRenovationDate;
    }

    public void setLastRenovationDate(LocalDate lastRenovationDate) {
        this.lastRenovationDate = LocalDate.now();
    }

    public int getCurrentDevices() {
        return currentDevices;
    }

    public void setCurrentDevices(int currentDevices) {
        this.currentDevices = currentDevices;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        if (Objects.equals(paymentFrequency, "Monthly") || Objects.equals(paymentFrequency, "Annually")) {
            this.paymentFrequency = paymentFrequency;
        }else{
            throw new IllegalArgumentException("Please choose a valid payment frequency!");
        }
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isSubscription() {
        return isSubscription;
    }

    public void setSubscription(boolean subscription) {
        isSubscription = subscription;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public String getJoke() {
        return joke;
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "subscriptionId=" + subscriptionId +
                ", planName='" + planName + '\'' +
                ", devices=" + devices +
                ", version=" + version +
                ", subscriptionDate=" + subscriptionDate +
                ", cancellationDate=" + cancellationDate +
                ", lastRenovationDate=" + lastRenovationDate +
                ", currentDevices=" + currentDevices +
                ", paymentFrequency='" + paymentFrequency + '\'' +
                ", isActive=" + isActive +
                ", isSubscription=" + isSubscription +
                ", userId=" + userId +
                ", joke='" + joke + '\'' +
                '}';
    }

    public Long calculateDate(LocalDate subscriptionDate, LocalDate cancellationDate){
        return ChronoUnit.DAYS.between(subscriptionDate, cancellationDate);
    }

    public void applyPatch(final String planName, final String paymentFrequency) {
        if (planName != null) {
            setPlanName(planName);
        }
        if (paymentFrequency != null) {
            setPaymentFrequency(paymentFrequency);
        }
    }

    public Subscription(final Long subscriptionId){
        setSubscriptionId(subscriptionId);
    }

    public Subscription(final String planName){
        setPlanName(planName);
    }

    public Subscription(final LocalDate subscriptionDate){
        setSubscriptionDate(subscriptionDate);
    }
}
