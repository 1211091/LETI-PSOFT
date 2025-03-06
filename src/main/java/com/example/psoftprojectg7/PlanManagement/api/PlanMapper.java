package com.example.psoftprojectg7.PlanManagement.api;

import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.PlanManagement.model.PriceHistory;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Mapper(componentModel = "spring")
public abstract class PlanMapper {

    public abstract PlanDTO toPlanDTO(Plan plan);
    public abstract Iterable<PriceHistoryDTO> toPriceHistoryDTO(Iterable<PriceHistory> priceHistoryList);
    public abstract Iterable<PlanDTO> toDTOList(Iterable<Plan> plans);

}
