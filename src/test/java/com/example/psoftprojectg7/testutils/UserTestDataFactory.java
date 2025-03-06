package com.example.psoftprojectg7.testutils;

import com.example.psoftprojectg7.UserManagement.api.CreateUserRequest;
import com.example.psoftprojectg7.UserManagement.api.UserView;
import com.example.psoftprojectg7.UserManagement.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
public class UserTestDataFactory {

    @Autowired
    private UserService userService;

    @Transactional
    public UserView createUser(final String username, final String fullName, final String password) {
        final CreateUserRequest createRequest = new CreateUserRequest(username, fullName, password);

        final UserView userView = userService.create(createRequest);

        assertNotNull(userView.getId(), "User id must not be null!");
        assertEquals(fullName, userView.getFullName(), "User name update isn't applied!");

        return userView;
    }

    public UserView createUser(final String username, final String fullName) {
        return createUser(username, fullName, "Test12345_");
    }

    @Transactional
    public void deleteUser(final Long id) {
        userService.delete(id);
    }

}
