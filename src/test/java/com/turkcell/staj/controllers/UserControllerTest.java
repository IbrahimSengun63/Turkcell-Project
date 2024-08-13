package com.turkcell.staj.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseGetUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseUpdateUserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void shouldAddUser() throws Exception {
        // Arrange
        RequestAddUserDTO request = new RequestAddUserDTO("name", "surname", 100.0);
        ResponseAddUserDTO response = new ResponseAddUserDTO(1, "name", "surname", 100.0);

        when(userService.addUser(any(RequestAddUserDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.surname").value("surname"))
                .andExpect(jsonPath("$.balance").value(100.0));

        verify(userService, times(1)).addUser(any(RequestAddUserDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenAddUserValidationsFailed() throws Exception {
        // Arrange
        RequestAddUserDTO request = new RequestAddUserDTO("", "", -10.0); // Invalid data

        // Act & Assert
        mockMvc.perform(post("/api/users/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).addUser(any(RequestAddUserDTO.class));
    }

    @Test
    void shouldGetUserById() throws Exception {
        // Arrange
        int id = 1;
        ResponseGetUserDTO response = new ResponseGetUserDTO(id, "name", "surname", 100.0);

        when(userService.getUser(id)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/users/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.surname").value("surname"))
                .andExpect(jsonPath("$.balance").value(100.0));

        verify(userService, times(1)).getUser(id);
    }

    @Test
    void shouldReturnBadRequestForInvalidIdWhenGettingUser() throws Exception {
        // Arrange
        int invalidId = -1;

        // Act & Assert
        mockMvc.perform(get("/api/users/{id}", invalidId))
                .andExpect(status().isBadRequest());

        verify(userService, never()).getUser(invalidId);
    }

    @Test
    void shouldUpdateUser() throws Exception {
        // Arrange
        int id = 1;
        RequestUpdateUserDTO request = new RequestUpdateUserDTO("name", "surname", 200.0);
        ResponseUpdateUserDTO response = new ResponseUpdateUserDTO(id, "name", "surname", 200.0);

        when(userService.updateUser(eq(id), any(RequestUpdateUserDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/users/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(id))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.surname").value("surname"))
                .andExpect(jsonPath("$.balance").value(200.0));

        verify(userService, times(1)).updateUser(eq(id), any(RequestUpdateUserDTO.class));
    }

    @Test
    void shouldReturnBadRequestWhenUpdateUserValidationsFailed() throws Exception {
        // Arrange
        int id = 1;
        RequestUpdateUserDTO request = new RequestUpdateUserDTO("", "", -20.0); // Invalid data

        // Act & Assert
        mockMvc.perform(put("/api/users/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(userService, never()).updateUser(eq(id), any(RequestUpdateUserDTO.class));
    }
}
