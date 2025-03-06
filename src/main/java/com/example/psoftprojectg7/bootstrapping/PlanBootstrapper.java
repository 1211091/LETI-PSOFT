
package com.example.psoftprojectg7.bootstrapping;
/*
*//*
 * Copyright (c) 2022-2022 the original author or authors.
 *
 * MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */



import com.example.psoftprojectg7.PlanManagement.model.Plan;
import com.example.psoftprojectg7.PlanManagement.repositories.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


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
public class PlanBootstrapper implements CommandLineRunner {

    private final PlanRepository planRepo;

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        if (planRepo.findByPlanName("Gold")==null) {
            final LocalDate date = LocalDate.of(2023,3,3);
            final Plan Gold = new Plan("Gold","Unlimited", "Later", 6, 25, "personalized", 5.99, 59.90, true, false,false, date, 4.5, 5.4, 6.5, 5.6);
            planRepo.save(Gold);
        }

        if (planRepo.findByPlanName("Silver")==null) {
            final LocalDate date1 = LocalDate.of(2023,4,4);
            final Plan Silver = new Plan("Silver","5000", "Later", 3, 10, "automatic", 4.99, 49.90, true, false,false, date1, 4.55, 5.45, 6.55, 5.65);
            planRepo.save(Silver);
        }

        if (planRepo.findByPlanName("Free")==null) {
            final LocalDate date2 = LocalDate.of(2023,4,4);
            final Plan Free = new Plan("Free","1000", "Later", 1, 0, "automatic", 0, 0, true, false,false, date2, 4.56, 5.46, 6.56, 5.66);
            planRepo.save(Free);
        }
    }
}

