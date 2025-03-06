/*
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
package com.example.psoftprojectg7.bootstrapping;



import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.psoftprojectg7.UserManagement.model.Role;
import com.example.psoftprojectg7.UserManagement.model.User;
import com.example.psoftprojectg7.UserManagement.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

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
public class UserBootstrapper implements CommandLineRunner {

	private final UserRepository userRepo;

	private final PasswordEncoder encoder;

	@Override
	@Transactional
	public void run(final String... args) throws Exception {
		// admin
		if (userRepo.findByUsername("sofia@gmail.com").isEmpty()) {
			final User u1 = new User("sofia@gmail.com", encoder.encode("sofs0586so!s"));
			u1.addAuthority(new Role(Role.USER_ADMIN));
			userRepo.save(u1);
		}

		// user
		if (userRepo.findByUsername("joaodomarketing@gmail.com").isEmpty()) {
			final User u2 = new User("joaodomarketing@gmail.com", encoder.encode("joNybo!2008"));
			u2.addAuthority(new Role(Role.MARKETING_DIRECTOR));
			userRepo.save(u2);
		}

		if (userRepo.findByUsername("carlota@gmail.com").isEmpty()) {
			final User u2 = User.newUser("carlota@gmail.com", encoder.encode("c@rl0586Bar"), "Carlota Barreiros", Role.SUBSCRIBER);
			userRepo.save(u2);
		}

		if (userRepo.findByUsername("fernandodagestaodeproduto@gmail.com").isEmpty()) {
			final User u2 = User.newUser("fernandodagestaodeproduto@gmail.com", encoder.encode("nand!nhoProducts50"), "Fernando Fernandes", Role.PRODUCT_MANAGER);
			userRepo.save(u2);
		}
	}
}

