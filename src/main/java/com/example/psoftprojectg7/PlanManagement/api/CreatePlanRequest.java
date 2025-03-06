package com.example.psoftprojectg7.PlanManagement.api;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreatePlanRequest extends EditPlanRequest{
 private String planName;
}

