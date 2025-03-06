package com.example.psoftprojectg7.SubscriptionManagement.api;

import com.example.psoftprojectg7.PlanManagement.model.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanDetailsRequest {
    private Plan plan;
}
