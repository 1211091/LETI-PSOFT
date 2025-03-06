package com.example.psoftprojectg7.PlanManagement.services;


import com.example.psoftprojectg7.PlanManagement.api.CreatePlanRequest;
import com.example.psoftprojectg7.PlanManagement.api.EditPlanRequest;
import com.example.psoftprojectg7.PlanManagement.api.EditPriceRequest;
import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.PlanManagement.model.PriceHistory;
import com.example.psoftprojectg7.UserManagement.model.User;

import java.util.List;
import java.util.Optional;

public interface PlanService {

    Iterable<Plan> findAll();

    Plan createPlan(CreatePlanRequest resource);

    Plan cancel(String planName);

    Plan update(String planName, EditPlanRequest resource);

    Plan updatePrice(String planName, EditPriceRequest resource,String userName);

    Plan cease(String planName);

    Plan promote(String planName);

    Iterable<PriceHistory> getPriceHistory(String planName);

}
