package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.exception.GlobalExceptionHandler;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        SessionController controller = new SessionController(sessionService, sessionMapper);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void invalidSessionIdReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/session/not-a-number"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid value for parameter 'id'"));
    }

    @Test
    void missingSessionReturnsNotFound() throws Exception {
        when(sessionService.getById(42L)).thenThrow(new NotFoundException("Session not found"));

        mockMvc.perform(get("/api/session/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Session not found"));
    }
}
