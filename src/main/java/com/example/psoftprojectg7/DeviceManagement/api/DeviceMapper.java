/**
 *
 * @author Carlos Alves
 *
 * Last update: 16/05/2023 20h55
 *
 */

package com.example.psoftprojectg7.DeviceManagement.api;
import com.example.psoftprojectg7.DeviceManagement.model.Device;
import java.util.Optional;

import com.example.psoftprojectg7.PlanManagement.api.PlanDTO;
import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.SubscriptionManagement.api.SubscriptionDTO;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import com.example.psoftprojectg7.SubscriptionManagement.repositories.SubscriptionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public abstract class DeviceMapper {
    public abstract DeviceDTO toDeviceDTO(Device device);
    public abstract Iterable<DeviceDTO> toDTOList(Iterable<Device> devices);



}
