package com.example.psoftprojectg7.DeviceManagement.model;

import org.hibernate.StaleObjectStateException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Table(name = "Devices")
@Entity
public class Device {

    @Column
    @NotNull
    //@NotBlank (message = "Device must be associated to a subscription")
    //private Long subscriptionId;
    private Long userId;

    @Id
    @Pattern(regexp = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$", message = "Invalid MAC address")
    @NotBlank(message = "MAC address is mandatory")
    @NotNull
    private String macAddress;

    @Column(length = 50)
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Designation should be alphanumeric")
    @NotBlank(message = "A designation must be provided. I.e: 'Maria's Tablet'")
    @NotNull
    private String designation;

    @Version
    private long version;

    public Device(){}

    public Device(Long userId, String macAddress, String designation) {
        //setSubscriptionId(subscriptionId);
        setUserId(userId);
        setMacAddress(macAddress);
        setDesignation(designation);
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /*
    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }
     */

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "Device{" +
                "macAddress='" + macAddress + '\'' +
                ", useerId=" + userId +
                //", subscriptionId=" + subscriptionId +
                ", designation='" + designation + '\'' +
                '}';
    }

    public void update(final long desiredVersion, final String designation){
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.macAddress);
        }
        // update data. if the request didn't contain a value for a field it will be
        // unset on this object. we do not allow to change the name attribute so we
        // simply ignore it
        setDesignation(designation);
        //setSubscriptionId(subscriptionId);
    }

    public void applyPatch(final long desiredVersion,final String designation) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.macAddress);
        }
        // apply patch only if the field was sent in the request.
        if (designation != null) {
            setDesignation(designation);
        }
    }

}
