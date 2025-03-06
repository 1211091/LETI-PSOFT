package com.example.psoftprojectg7.DeviceManagement.api;

import com.example.psoftprojectg7.DeviceManagement.services.DeviceService;
import com.example.psoftprojectg7.SubscriptionManagement.api.Helper;
import com.example.psoftprojectg7.UserManagement.model.Role;
import com.example.psoftprojectg7.UserManagement.model.User;
import com.example.psoftprojectg7.UserManagement.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Iterator;
import java.util.Set;

@Tag(name = "Devices", description = "Endpoints for managing user devices")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/devices")

public class DeviceController {

    private final DeviceService deviceService;
    private final DeviceMapper deviceMapper;
    private final UserRepository userRepository;

    @Autowired
    private Helper helper;

    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }

    @Operation(summary = "Creates or updates a device")
    @PutMapping("/{macAddress}")
    @RolesAllowed(Role.SUBSCRIBER)
    public ResponseEntity<DeviceDTO> upsert(
            final HttpServletRequest request,
            @PathVariable("macAddress") final String macAddress,
            @Valid @RequestBody final CreateDeviceRequest resource)
    {
        //Authentication
        long userId = helper.getUserByToken(request);

        User user = userRepository.getById(userId);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=SUBSCRIBER)";

        if(firstRole.toString().equals(role)) {
            final String ifMatchValue = request.getHeader("If-Match");
            if (ifMatchValue == null || ifMatchValue.isEmpty()) {
                // no if-match header was sent, so we are in INSERT mode
                final var device = deviceService.createDevice(macAddress, userId, resource);
                final var newDeviceUri = ServletUriComponentsBuilder.fromCurrentRequestUri().pathSegment(device.getMacAddress()).build().toUri();

                return ResponseEntity.created(newDeviceUri).eTag(Long.toString(device.getVersion()))
                        .body(deviceMapper.toDeviceDTO(device));
            }
            // if-match header was sent, so we are in UPDATE mode
            final var device = deviceService.update(macAddress, resource, getVersionFromIfMatchHeader(ifMatchValue));
            return ResponseEntity.ok().eTag(Long.toString(device.getVersion())).body(deviceMapper.toDeviceDTO(device));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only subscribers may create or update a device!");
        }
    }

    @Operation(summary = "Gets all devices")
    @GetMapping
    @RolesAllowed(Role.USER_ADMIN)
    public Iterable<DeviceDTO> getAllDevices(final HttpServletRequest request) {

        long userId = helper.getUserByToken(request);

        User user = userRepository.getById(userId);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=SUBSCRIBER)";

        if (firstRole.toString().equals(role)) {
            return deviceMapper.toDTOList(deviceService.findAll());
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only subscribers may list devices!");
        }

    }

    @Operation(summary = "Deletes an existing device")
    @DeleteMapping(value = "/{macAddress}")
    @RolesAllowed(Role.SUBSCRIBER)
    public ResponseEntity<DeviceDTO> delete(
            final HttpServletRequest request,
            @Valid @PathVariable("macAddress") final String macAddress) {

        //Authentication
        long id = helper.getUserByToken(request);

        User user = userRepository.getById(id);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=SUBSCRIBER)";

        if (firstRole.toString().equals(role)) {
            final int count = deviceService.deleteById(macAddress);

            // TODO check if we can distinguish between a 404 and a 409
            return count == 1 ? ResponseEntity.noContent().build() : ResponseEntity.status(409).build();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only subscribers may delete a device!");
        }
    }


    @Operation(summary = "Gets a subscription's devices")
    @RolesAllowed(Role.SUBSCRIBER)
    @GetMapping("/user")
    public Iterable<DeviceDTO> getSubsDevices(HttpServletRequest request)
    {
        long userId = helper.getUserByToken(request);

        User user = userRepository.getById(userId);

        Set<Role> userRoles = user.getAuthorities();

        Iterator<Role> iterator = userRoles.iterator();

        Role firstRole = iterator.next();

        String role = "Role(authority=SUBSCRIBER)";

        if (firstRole.toString().equals(role)) {
            return deviceMapper.toDTOList(deviceService.findDevSubs(userId));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Only subscribers may access a subscription's devices!");
        }
    }
}
