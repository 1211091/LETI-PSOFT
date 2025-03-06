package com.example.psoftprojectg7.SubscriptionManagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditSubscriptionRequest {
    private String planName;
    private String paymentFrequency;
}
