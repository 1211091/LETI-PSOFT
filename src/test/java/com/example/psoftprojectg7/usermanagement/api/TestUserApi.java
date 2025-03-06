package com.example.psoftprojectg7.usermanagement.api;

import static java.lang.System.currentTimeMillis;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.psoftprojectg7.UserManagement.api.CreateUserRequest;
import com.example.psoftprojectg7.UserManagement.api.EditUserRequest;
import com.example.psoftprojectg7.UserManagement.api.UserView;
import com.example.psoftprojectg7.UserManagement.model.Role;
import com.example.psoftprojectg7.testutils.JsonHelper;
import com.example.psoftprojectg7.testutils.UserTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Based on https://github.com/Yoh0xFF/java-spring-security-example
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(roles = Role.USER_ADMIN)
class TestUserApi {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserTestDataFactory userTestDataFactory;

    @Autowired
    public TestUserApi(final MockMvc mockMvc, final ObjectMapper objectMapper,
                            final UserTestDataFactory userTestDataFactory) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userTestDataFactory = userTestDataFactory;
    }

    @Test
    void testCreateSuccess() throws Exception {
        final CreateUserRequest goodRequest = new CreateUserRequest(
                String.format("test.user.%d@nix.com", currentTimeMillis()), "Test User A", "Test12345_");

        final MvcResult createResult = this.mockMvc.perform(post("/api/admin/user")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(objectMapper, goodRequest)))
                .andExpect(status().isOk()).andReturn();

        final UserView userView = JsonHelper.fromJson(objectMapper, createResult.getResponse().getContentAsString(),
                UserView.class);
        assertNotNull(userView.getId(), "User id must not be null!");
        assertEquals(goodRequest.getFullName(), userView.getFullName(), "User fullname update isn't applied!");
    }

    @Test
    void testCreateFail() throws Exception {
        final CreateUserRequest badRequest = new CreateUserRequest("invalid.username", "", "");

        this.mockMvc
                .perform(post("/api/admin/user").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, badRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Method argument validation failed")));
    }

    @Test
    void testCreateUsernameExists() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@nix.io", currentTimeMillis()), "Test User A");

        final CreateUserRequest badRequest = new CreateUserRequest(userView.getUsername(), "Test User A", "Test12345_");

        this.mockMvc
                .perform(post("/api/admin/user").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, badRequest)))
                .andExpect(status().isConflict())
                .andExpect(content().string(containsString("Username already exists")));
    }

    @Test
    void testCreatePasswordsMismatch() throws Exception {
        final CreateUserRequest badRequest = new CreateUserRequest(
                String.format("test.user.%d@nix.com", currentTimeMillis()), "Test User A", "Test12345_");
        badRequest.setRePassword("otherPass");

        this.mockMvc
                .perform(post("/api/admin/user").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, badRequest)))
                //.andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Passwords don't match")));
    }

    @Test
    void testEditSuccess() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@nix.io", currentTimeMillis()), "Test User A");

        final EditUserRequest updateRequest = new EditUserRequest("Test User B", null);

        final MvcResult updateResult = this.mockMvc.perform(put(String.format("/api/admin/user/%s", userView.getId()))
                        .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(objectMapper, updateRequest)))
                .andExpect(status().isOk()).andReturn();
        final UserView newUserView = JsonHelper.fromJson(objectMapper, updateResult.getResponse().getContentAsString(),
                UserView.class);

        assertEquals(updateRequest.getFullName(), newUserView.getFullName(), "User fullname update isn't applied!");
    }

    @Test
    void testEditFailBadRequest() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@nix.io", currentTimeMillis()), "Test User A");

        final EditUserRequest updateRequest = new EditUserRequest();

        this.mockMvc
                .perform(put(String.format("/api/admin/user/%s", userView.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, updateRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Method argument validation failed")));
    }

    @Test
    void testEditFailNotFound() throws Exception {
        final EditUserRequest updateRequest = new EditUserRequest("Test User B", null);

        this.mockMvc
                .perform(put(String.format("/api/admin/user/%s", "0")).contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, updateRequest)))
                .andExpect(status().isNotFound()).andExpect(content().string(containsString("Not found")));
    }

    @Test
    void testDeleteSuccess() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@nix.io", currentTimeMillis()), "Test User A");

        this.mockMvc.perform(delete(String.format("/api/admin/user/%s", userView.getId()))).andExpect(status().isOk());

        this.mockMvc.perform(get(String.format("/api/admin/user/%s", userView.getId())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteFailNotFound() throws Exception {
        this.mockMvc.perform(delete(String.format("/api/admin/user/%s", "0"))).andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Not found")));
    }
/*
    @Test
    void testDeleteAndCreateAgain() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@niix.io", currentTimeMillis()), "User A");

        this.mockMvc.perform(delete(String.format("/api/admin/user/%s", userView.getId()))).andExpect(status().isOk());

        this.mockMvc.perform(get(String.format("/api/admin/user/%s", userView.getId())))
                .andExpect(status().isNotFound());

        final CreateUserRequest goodRequest = new CreateUserRequest(userView.getUsername(), "User A",
                "Test12345_");

        final MvcResult createResult = this.mockMvc.perform(post("/api/admin/user")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(objectMapper, goodRequest)))
                .andExpect(status().isOk()).andReturn();

        final UserView newUserView = JsonHelper.fromJson(objectMapper, createResult.getResponse().getContentAsString(),
                UserView.class);
        assertNotEquals(userView.getId(), newUserView.getId(), "User ids must not match!");
        assertEquals(userView.getUsername(), newUserView.getUsername(), "User names must match!");
    }
*/
    @Test
    void testGetSuccess() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@nix.io", currentTimeMillis()), "Test User A");

        final MvcResult getResult = this.mockMvc.perform(get(String.format("/api/admin/user/%s", userView.getId())))
                .andExpect(status().isOk()).andReturn();

        final UserView newUserView = JsonHelper.fromJson(objectMapper, getResult.getResponse().getContentAsString(),
                UserView.class);

        assertEquals(userView.getId(), newUserView.getId(), "User ids must be equal!");
    }

    @Test
    void testGetNotFound() throws Exception {
        this.mockMvc.perform(get(String.format("/api/admin/user/%s", "0"))).andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Not found")));
    }

}
