package com.example.psoftprojectg7.SubscriptionManagement.services;

import com.example.psoftprojectg7.SubscriptionManagement.api.CreateSubscriptionRequest;
import com.example.psoftprojectg7.SubscriptionManagement.api.EditSubscriptionRequest;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import com.example.psoftprojectg7.UserManagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class EditSubscriptionMapper {

    public abstract Subscription create(CreateSubscriptionRequest resource);

    public abstract void update(EditSubscriptionRequest request, @MappingTarget Subscription subscription);
}
