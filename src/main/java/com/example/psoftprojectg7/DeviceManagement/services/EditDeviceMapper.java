package com.example.psoftprojectg7.DeviceManagement.services;

import com.example.psoftprojectg7.DeviceManagement.api.EditDeviceRequest;
import com.example.psoftprojectg7.DeviceManagement.model.Device;
import com.example.psoftprojectg7.PlanManagement.api.EditPlanRequest;
import com.example.psoftprojectg7.PlanManagement.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public abstract class EditDeviceMapper {
    public abstract Device create(String macAddress, Long userId, EditDeviceRequest request);

    public abstract void update(EditDeviceRequest request, @MappingTarget Device device);

}
