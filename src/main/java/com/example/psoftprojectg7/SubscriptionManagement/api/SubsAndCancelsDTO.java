package com.example.psoftprojectg7.SubscriptionManagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "A Subscription")
public class SubsAndCancelsDTO {
    private List<Integer> subsAndCancels;
}
