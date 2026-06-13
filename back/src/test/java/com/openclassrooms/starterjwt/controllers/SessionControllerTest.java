package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.GlobalExceptionHandler;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SessionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@ContextConfiguration(classes = {SessionController.class, GlobalExceptionHandler.class})
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private SessionMapper sessionMapper;

    @Test
    void findByIdReturnsSession() throws Exception {
        Session session = Session.builder().id(1L).build();
        SessionDto dto = validDto();
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(dto);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga"));
    }

    @Test
    void findAllReturnsSessions() throws Exception {
        List<Session> sessions = List.of(Session.builder().id(1L).build());
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(List.of(validDto()));

        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Yoga"));
    }

    @Test
    void createDelegatesToService() throws Exception {
        SessionDto dto = validDto();
        Session entity = Session.builder().name(dto.getName()).build();
        Session saved = Session.builder().id(1L).name(dto.getName()).build();
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(entity);
        when(sessionService.create(entity, 2L, List.of(3L))).thenReturn(saved);
        when(sessionMapper.toDto(saved)).thenReturn(dto);

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga"));

        verify(sessionService).create(entity, 2L, List.of(3L));
    }

    @Test
    void createRejectsInvalidPayload() throws Exception {
        SessionDto dto = validDto();
        dto.setName("");

        mockMvc.perform(post("/api/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    void updateDelegatesToService() throws Exception {
        SessionDto dto = validDto();
        Session changes = Session.builder().name(dto.getName()).build();
        Session updated = Session.builder().id(1L).name(dto.getName()).build();
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(changes);
        when(sessionService.update(1L, changes, 2L)).thenReturn(updated);
        when(sessionMapper.toDto(updated)).thenReturn(dto);

        mockMvc.perform(put("/api/session/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga"));
    }

    @Test
    void deleteDelegatesToService() throws Exception {
        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());

        verify(sessionService).delete(1L);
    }

    @Test
    void participateDelegatesToService() throws Exception {
        mockMvc.perform(post("/api/session/1/participate/2"))
                .andExpect(status().isOk());

        verify(sessionService).participate(1L, 2L);
    }

    @Test
    void noLongerParticipateDelegatesToService() throws Exception {
        mockMvc.perform(delete("/api/session/1/participate/2"))
                .andExpect(status().isOk());

        verify(sessionService).noLongerParticipate(1L, 2L);
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

    @Test
    void duplicateParticipationReturnsBadRequest() throws Exception {
        org.mockito.Mockito.doThrow(new BadRequestException("Already registered"))
                .when(sessionService).participate(1L, 2L);

        mockMvc.perform(post("/api/session/1/participate/2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Already registered"));
    }

    private SessionDto validDto() {
        return new SessionDto(
                1L,
                "Yoga",
                new Date(),
                2L,
                "A yoga session",
                List.of(3L),
                null,
                null
        );
    }
}
