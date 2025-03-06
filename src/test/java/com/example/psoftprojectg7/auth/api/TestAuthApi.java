package com.example.psoftprojectg7.auth.api;

import static java.lang.System.currentTimeMillis;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.psoftprojectg7.UserManagement.api.CreateUserRequest;
import com.example.psoftprojectg7.UserManagement.api.UserView;
import com.example.psoftprojectg7.testutils.JsonHelper;
import com.example.psoftprojectg7.testutils.UserTestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TestAuthApi {


    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final UserTestDataFactory userTestDataFactory;

    private final String password = "Test12345_";

    @Autowired
    public TestAuthApi(final MockMvc mockMvc, final ObjectMapper objectMapper,
                       final UserTestDataFactory userTestDataFactory) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.userTestDataFactory = userTestDataFactory;
    }

    @Test
    void testLoginSuccess() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@nix.io", currentTimeMillis()), "Test User", password);

        final AuthRequest request = new AuthRequest(userView.getUsername(), password);

        final MvcResult createResult = this.mockMvc
                .perform(post("/api/public/login").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, request)))
                .andExpect(status().isOk()).andExpect(header().exists(HttpHeaders.AUTHORIZATION)).andReturn();

        final UserView authUserView = JsonHelper.fromJson(objectMapper, createResult.getResponse().getContentAsString(),
                UserView.class);
        assertEquals(userView.getId(), authUserView.getId(), "User ids must match!");
    }

    @Test
    void testLoginFail() throws Exception {
        final UserView userView = userTestDataFactory
                .createUser(String.format("test.user.%d@nix.io", currentTimeMillis()), "Test User", password);

        final AuthRequest request = new AuthRequest(userView.getUsername(), "zxc");

        this.mockMvc
                .perform(post("/api/public/login").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, request)))
                .andExpect(status().isUnauthorized()).andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andReturn();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        final CreateUserRequest goodRequest = new CreateUserRequest(
                String.format("test.user.%d@nix.com", currentTimeMillis()), "Test User A", password);

        final MvcResult createResult = this.mockMvc.perform(post("/api/public/register")
                        .contentType(MediaType.APPLICATION_JSON).content(JsonHelper.toJson(objectMapper, goodRequest)))
                .andExpect(status().isOk()).andReturn();

        final UserView userView = JsonHelper.fromJson(objectMapper, createResult.getResponse().getContentAsString(),
                UserView.class);
        assertNotNull(userView.getId(), "User id must not be null!");
        assertEquals(goodRequest.getFullName(), userView.getFullName(), "User fullname  update isn't applied!");
    }

    @Test
    void testRegisterFail() throws Exception {
        final CreateUserRequest badRequest = new CreateUserRequest("invalid.username", "", "");

        this.mockMvc
                .perform(post("/api/public/register").contentType(MediaType.APPLICATION_JSON)
                        .content(JsonHelper.toJson(objectMapper, badRequest)))
                .andExpect(status().isBadRequest()).andExpect(content().string(containsString("must not be blank")));
    }

}
