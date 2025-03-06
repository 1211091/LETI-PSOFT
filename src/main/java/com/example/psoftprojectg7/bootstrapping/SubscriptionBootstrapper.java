package com.example.psoftprojectg7.bootstrapping;
/*

import com.example.psoftprojectg7.DeviceManagement.model.Device;
import com.example.psoftprojectg7.SubscriptionManagement.model.*;
import com.example.psoftprojectg7.SubscriptionManagement.repositories.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

*/

import com.example.psoftprojectg7.DeviceManagement.model.Device;
import com.example.psoftprojectg7.SubscriptionManagement.model.Subscription;
import com.example.psoftprojectg7.SubscriptionManagement.repositories.SubscriptionRepository;
import com.example.psoftprojectg7.UserManagement.model.Role;
import com.example.psoftprojectg7.UserManagement.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Spring will load and execute all components that implement the interface
 * CommandLinerunner on startup, so we will use that as a way to bootstrap some
 * data for testing purposes.
 * <p>
 * In order to enable this bootstraping make sure you activate the spring
 * profile "bootstrap" in application.properties
 *
 * @author pgsou
 *
*/

@Component
@RequiredArgsConstructor
@Profile("bootstrap")
public class SubscriptionBootstrapper implements CommandLineRunner {

    private final SubscriptionRepository subRepo;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        if (subRepo.findById(1L).isEmpty()) {
            ArrayList<Device> list = new ArrayList<>();
            final Device device = new Device(5L,"11:22:33:44:55:66:77:88", "bom");
            list.add(device);
            final LocalDate subDate = LocalDate.of(2023, 1, 19);
            final LocalDate cancelDate = LocalDate.of(2023, 3, 21);
            final LocalDate lastRenoDate = LocalDate.of(2023, 2, 20);
            final Subscription silver = new Subscription("Silver", list, subDate, cancelDate, lastRenoDate, 1, "Monthly", true, true, 1L, "Deixa para amanhã o que podes fazer hoje");
            subRepo.save(silver);
        }
        if (subRepo.findById(2L).isEmpty()) {
            List<Device> list1 = new ArrayList<>();
            final Device device1 = new Device(5L, "11:22:33:44:55:66:77:99", "fixe");
            list1.add(device1);
            final LocalDate subDate1 = LocalDate.of(2023, 2, 15);
            final LocalDate cancelDate1 = LocalDate.of(2023, 4, 25);
            final LocalDate lastRenoDate1 = LocalDate.of(2023, 3, 3);
            final Subscription gold = new Subscription("Gold", list1, subDate1, cancelDate1, lastRenoDate1, 1, "Annually", true, true, 2L, "Quando a vida te da limões tempera a salada");
            subRepo.save(gold);
        }

    }
}

