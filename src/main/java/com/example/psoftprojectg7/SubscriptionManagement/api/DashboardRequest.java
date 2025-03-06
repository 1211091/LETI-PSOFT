package com.example.psoftprojectg7.SubscriptionManagement.api;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardRequest {
    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    private int month;

}
