package com.example.psoftprojectg7.Dashboard.api;

import com.example.psoftprojectg7.Dashboard.model.RevenueObj;
import com.example.psoftprojectg7.DeviceManagement.api.DeviceDTO;
import com.example.psoftprojectg7.DeviceManagement.model.Device;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public abstract class RevenueMapper {

    public RevenueDTO toRevenueDTO(List<String> columns, List<RevenueObj> rows) {
        RevenueDTO revenueDTO = new RevenueDTO();

        revenueDTO.setColumns(columns);
        revenueDTO.setRows(rows);

        return revenueDTO;
    }

}