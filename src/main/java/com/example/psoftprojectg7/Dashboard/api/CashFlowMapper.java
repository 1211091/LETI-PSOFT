package com.example.psoftprojectg7.Dashboard.api;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class CashFlowMapper {
    public CashFlowDTO toCashFlowDTO(List<String> columns, List<List<Object>> rows) {
        CashFlowDTO cashFlowDTO = new CashFlowDTO();

        cashFlowDTO.setColumns(columns);
        cashFlowDTO.setRows(new ArrayList<>(rows));

        return cashFlowDTO;
    }
}
