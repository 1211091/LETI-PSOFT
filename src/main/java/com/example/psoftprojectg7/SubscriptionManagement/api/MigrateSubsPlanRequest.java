package com.example.psoftprojectg7.SubscriptionManagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MigrateSubsPlanRequest {
    private String newPlanName;
}
