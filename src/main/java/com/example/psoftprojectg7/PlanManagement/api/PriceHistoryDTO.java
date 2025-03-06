package com.example.psoftprojectg7.PlanManagement.api;

import com.example.psoftprojectg7.PlanManagement.model.PriceHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Price history")
public class PriceHistoryDTO {
    private String userName;
    private String planName;
    private LocalDate lastPriceChangeDate;
    private double previousMonthlyFee;
    private double previousAnnualFee;
    private double currentMonthlyFee;
    private double currentAnnualFee;
}
