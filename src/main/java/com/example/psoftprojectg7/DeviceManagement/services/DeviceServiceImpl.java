package com.example.psoftprojectg7.DeviceManagement.services;

import com.example.psoftprojectg7.DeviceManagement.api.EditDeviceRequest;
import com.example.psoftprojectg7.DeviceManagement.model.Device;
import com.example.psoftprojectg7.DeviceManagement.repositories.DeviceRepository;
import com.example.psoftprojectg7.PlanManagement.api.EditPlanRequest;
import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import com.example.psoftprojectg7.SubscriptionManagement.repositories.SubscriptionRepository;
import com.example.psoftprojectg7.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {

    private DeviceRepository deviceRepository;
    private SubscriptionRepository subscriptionRepository;
    private EditDeviceMapper editDeviceMapper;

    @Autowired
    public DeviceServiceImpl(DeviceRepository deviceRepository, EditDeviceMapper editDeviceMapper,SubscriptionRepository subscriptionRepository) {
        this.deviceRepository = deviceRepository;
        this.editDeviceMapper = editDeviceMapper;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public Iterable<Device> findAll() {
        return deviceRepository.findAll();
    }

    @Override
    @Transactional
    public Device createDevice(final String macAddress, final Long userId, final EditDeviceRequest resource) {
        // construct a new object based on data received by the service
        final Subscription subscription = subscriptionRepository.findByUserId(userId);

        final Device device = editDeviceMapper.create(macAddress, userId, resource);

        // Adiconar dispositivo à subscrição
        List<Device> devices = subscription.getDevices();
        devices.add(device);
        subscription.setDevices(devices);
        //subscription.getDevices().add(device);

        // incrementar o número de dispositivos na subscrição
        subscription.setCurrentDevices(subscription.getCurrentDevices() + 1);

        subscriptionRepository.save(subscription);

        return deviceRepository.save(device);

    }

    @Override
    public Device update(final String macAddress, final EditDeviceRequest resource, final long desiredVersion) {
        // first let's check if the object exists so we don't create a new object with
        // save
        final var device = deviceRepository.findById(macAddress)
                .orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));

        // apply update
        device.update(desiredVersion, resource.getDesignation());

        return deviceRepository.save(device);
    }

    /*

    @Override
    public Device partialUpdate(final String macAddress, final EditDeviceRequest resource, final long desiredVersion) {
        // first let's check if the object exists so we don't create a new object with save
        final var device = deviceRepository.findById(macAddress)
                .orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));

        // we need to search the related Subscription object so we can reference it in the new Device
        final var subscription = subscriptionRepository.findById(resource.getSubscriptionId())
                .orElseThrow(() -> new ValidationException("Select an existing subscription"));

        // since we got the object from the database we can check the version in memory
        // and apply the patch
        device.applyPatch(desiredVersion, resource.getDesignation());

        //update the device inside of the subscription's device list
        // https://stackoverflow.com/questions/55624006/update-a-single-object-from-a-list-using-stream

        subscription.getDevices().stream()
                                 .filter(d -> d.getMacAddress().equals(macAddress))
                                 .peek(d -> d.setDesignation(resource.getDesignation()));

        subscriptionRepository.save(subscription);

        return deviceRepository.save(device);
    }*/

    @Override
    @Transactional
    public int deleteById(final String macAddress) {
        final var device = deviceRepository.findById(macAddress);

        final Subscription subscription = subscriptionRepository.findByUserId(device.get().getUserId());

        subscription.getDevices().remove(device.get());
        subscription.setCurrentDevices(subscription.getCurrentDevices() - 1);

        subscriptionRepository.save(subscription);

        return deviceRepository.deleteByMacAddress(macAddress);
    }

    @Override
    public Iterable<Device> findDevSubs(Long userId) {
        //final var sub = subscriptionRepository.findByUserId(userId);

        return deviceRepository.findByUserId(userId);

    }


}

