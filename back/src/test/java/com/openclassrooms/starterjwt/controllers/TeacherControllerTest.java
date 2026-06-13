package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.exception.GlobalExceptionHandler;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TeacherController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
@ContextConfiguration(classes = {TeacherController.class, GlobalExceptionHandler.class})
class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TeacherService teacherService;

    @MockitoBean
    private TeacherMapper teacherMapper;

    @Test
    void findByIdReturnsTeacher() throws Exception {
        Teacher teacher = Teacher.builder().id(1L).build();
        TeacherDto dto = new TeacherDto(1L, "Last", "First", null, null);
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(dto);

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("First"));
    }

    @Test
    void findAllReturnsTeachers() throws Exception {
        List<Teacher> teachers = List.of(Teacher.builder().id(1L).build());
        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers))
                .thenReturn(List.of(new TeacherDto(1L, "Last", "First", null, null)));

        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lastName").value("Last"));
    }

    @Test
    void findByIdReturnsNotFoundForUnknownTeacher() throws Exception {
        when(teacherService.findById(42L)).thenThrow(new NotFoundException("Teacher not found"));

        mockMvc.perform(get("/api/teacher/42"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Teacher not found"));
    }
}
