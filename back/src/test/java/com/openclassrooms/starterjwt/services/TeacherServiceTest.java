package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    void findAllReturnsTeachers() {
        List<Teacher> teachers = List.of(Teacher.builder().id(1L).build());
        when(teacherRepository.findAll()).thenReturn(teachers);

        assertSame(teachers, teacherService.findAll());
    }

    @Test
    void findByIdReturnsTeacher() {
        Teacher teacher = Teacher.builder().id(1L).build();
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        assertSame(teacher, teacherService.findById(1L));
    }

    @Test
    void findByIdRejectsUnknownTeacher() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> teacherService.findById(1L));
    }
}
