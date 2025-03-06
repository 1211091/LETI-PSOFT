package com.example.psoftprojectg7.PlanManagement.model;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "PriceHistory")
@Entity
public class PriceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long priceId;
    private LocalDate lastPriceChangeDate;
    private double previousMonthlyFee = 0;
    private double previousAnnualFee = 0;
    private double currentMonthlyFee = 0;
    private double currentAnnualFee = 0;
    private String planName;
    private String username;

    public PriceHistory(LocalDate lastPriceChangeDate, double previousMonthlyFee, double previousAnnualFee, double currentMonthlyFee, double currentAnnualFee, String planName, String username) {
        this.lastPriceChangeDate = lastPriceChangeDate;
        this.previousMonthlyFee = previousMonthlyFee;
        this.previousAnnualFee = previousAnnualFee;
        this.currentMonthlyFee = currentMonthlyFee;
        this.currentAnnualFee = currentAnnualFee;
        this.planName = planName;
        this.username = username;
    }

    public PriceHistory(){}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getLastPriceChangeDate() {
        return lastPriceChangeDate;
    }

    public void setLastPriceChangeDate(LocalDate lastPriceChangeDate) {
        this.lastPriceChangeDate = lastPriceChangeDate;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public double getPreviousMonthlyFee() {
        return previousMonthlyFee;
    }

    public void setPreviousMonthlyFee(double previousMonthlyFee) {
        this.previousMonthlyFee = previousMonthlyFee;
    }

    public double getPreviousAnnualFee() {
        return previousAnnualFee;
    }

    public void setPreviousAnnualFee(double previousAnnualFee) {
        this.previousAnnualFee = previousAnnualFee;
    }

    public double getCurrentMonthlyFee() {
        return currentMonthlyFee;
    }

    public void setCurrentMonthlyFee(double currentMonthlyFee) {
        this.currentMonthlyFee = currentMonthlyFee;
    }

    public double getCurrentAnnualFee() {
        return currentAnnualFee;
    }

    public void setCurrentAnnualFee(double currentAnnualFee) {
        this.currentAnnualFee = currentAnnualFee;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}
