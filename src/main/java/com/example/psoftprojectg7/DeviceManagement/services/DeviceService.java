package com.example.psoftprojectg7.DeviceManagement.services;

import com.example.psoftprojectg7.DeviceManagement.api.EditDeviceRequest;
import com.example.psoftprojectg7.DeviceManagement.model.Device;
import com.example.psoftprojectg7.PlanManagement.api.EditPlanRequest;
import com.example.psoftprojectg7.PlanManagement.model.Plan;

import java.util.List;

public interface DeviceService {
    Device createDevice(String macAddress, Long userId, EditDeviceRequest resource);

    Device update(String macAddress, EditDeviceRequest resource, long desiredVersion);

    //Device partialUpdate(String macAddress, EditDeviceRequest resource, long desiredVersion);
    Iterable<Device> findAll();

    int deleteById(String macAddress);

    Iterable<Device> findDevSubs(Long userId);

}
