package com.example.psoftprojectg7.DeviceManagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;

@Data
@Schema(description = "A Device")

public class DeviceDTO {
    //private Long subscriptionId;
    private Long userId;
    private String macAddress;
    private String designation;
}
