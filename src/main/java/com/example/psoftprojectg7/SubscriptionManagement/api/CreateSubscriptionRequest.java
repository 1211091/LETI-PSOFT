package com.example.psoftprojectg7.SubscriptionManagement.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateSubscriptionRequest extends EditSubscriptionRequest{
    private Long userId;
}
