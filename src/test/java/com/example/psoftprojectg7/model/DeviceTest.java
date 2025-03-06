package com.example.psoftprojectg7.model;

import static junit.framework.TestCase.assertEquals;

import com.example.psoftprojectg7.DeviceManagement.api.EditDeviceRequest;
import com.example.psoftprojectg7.DeviceManagement.model.Device;
import org.hibernate.StaleObjectStateException;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertThrows;

public class DeviceTest {

    @Test
    public void ensureMacAddressIsValid() {
        assertThrows(IllegalArgumentException.class, () -> new Device(1L,"A1:B2:C3:D4:FF-KL", "designation"));
    }

    @Test
    public void ensureMacAddressIsSet() {
        final var subject = new Device(null,"A1:B2:C3:D4:FF:FF", "designation");
        assertEquals("A1:B2:C3:D4:FF:FF", subject.getMacAddress());
    }

    @Test
    public void ensureUserIdMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Device(null,"A1:B2:C3:D4:FF:FF", "designation"));
    }

    @Test
    public void ensureUserIdIsSet() {
        final var subject = new Device(1L,"A1:B2:C3:D4:FF:FF", "designation");
        assertEquals(Optional.of(1L), subject.getUserId());
    }

    @Test
    public void ensureDesignationMustNotBeNull() {
        assertThrows(IllegalArgumentException.class, () -> new Device(1L,"A1:B2:C3:D4:FF:FF", null));
    }

    @Test
    public void ensureDesignationMustNotBeBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Device(1L,"A1:B2:C3:D4:FF:FF", ""));
    }

    @Test
    public void ensureDesignationIsSet() {
        final var subject = new Device(1L,"A1:B2:C3:D4:FF:FF", "designation");
        assertEquals("designation", subject.getDesignation());
    }

    @Test
    public void whenVersionIsStaledItIsNotPossibleToPatch() {
        final var patch = new EditDeviceRequest("new designation");
        final var subject = new Device(1L,"A1:B2:C3:D4:FF:FF", "designation");

        assertThrows(StaleObjectStateException.class,
                () -> subject.applyPatch(999, patch.getDesignation()));
    }

    /*
    @Test
    void ensurePatchDesignation() {
        final var patch = new EditDeviceRequest(1L, "new designation");

        final var subject = new Device(1L,"A1:B2:C3:D4:FF:FF", "designation");
        subject.setDesignation("designation");

        subject.applyPatch(0, patch.getDesignation());

        assertEquals(Integer.valueOf(50), subject.getWidth().get());
    }*/

}
