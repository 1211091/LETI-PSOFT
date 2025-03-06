package com.example.psoftprojectg7.PlanManagement.services;

import com.example.psoftprojectg7.PlanManagement.api.CreatePlanRequest;
import com.example.psoftprojectg7.PlanManagement.api.EditPlanRequest;
import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.PlanManagement.repositories.PlanRepository;
import com.example.psoftprojectg7.SubscriptionManagement.repositories.SubscriptionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class EditPlanMapper {

    public abstract Plan create(CreatePlanRequest request);

    public abstract void update(EditPlanRequest request, @MappingTarget Plan plan);
}

