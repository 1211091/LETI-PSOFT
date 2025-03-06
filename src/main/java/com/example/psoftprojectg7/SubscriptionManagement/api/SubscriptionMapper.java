package com.example.psoftprojectg7.SubscriptionManagement.api;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper (componentModel = "spring")
public abstract class SubscriptionMapper {
    public abstract SubscriptionDTO toSubscriptionDTO(Subscription subscription);
    public abstract JokeDTO toJokeDTO(String joke);
    public abstract Iterable<SubscriptionDTO> toDTOList(Iterable<Subscription> subscriptions);

}
