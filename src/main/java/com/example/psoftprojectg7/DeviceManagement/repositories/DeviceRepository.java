package com.example.psoftprojectg7.DeviceManagement.repositories;
import com.example.psoftprojectg7.DeviceManagement.model.Device;
import com.example.psoftprojectg7.PlanManagement.model.Plan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DeviceRepository extends CrudRepository<Device, String> {
    Optional<Device> findById(String macAddress);
    //Iterable<Device> findBySubscriptionId(Long subscriptionId);
    Iterable<Device> findByUserId(Long userId);
    @Modifying
    @Query("DELETE FROM Device f WHERE f.macAddress = ?1")
    int deleteByMacAddress(String macAddress);
}
