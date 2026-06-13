package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService(sessionRepository, teacherRepository, userRepository);
    }

    @Test
    void getByIdReturnsSession() {
        Session session = Session.builder().id(1L).build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertSame(session, sessionService.getById(1L));
    }

    @Test
    void getByIdThrowsWhenSessionDoesNotExist() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.getById(1L));
    }

    @Test
    void createResolvesTeacherAndUsers() {
        Teacher teacher = Teacher.builder().id(2L).build();
        User user = user(3L);
        Session session = validSession();

        when(teacherRepository.findById(2L)).thenReturn(Optional.of(teacher));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        Session created = sessionService.create(session, 2L, List.of(3L));

        assertSame(teacher, created.getTeacher());
        assertEquals(List.of(user), created.getUsers());
        verify(sessionRepository).save(session);
    }

    @Test
    void updatePreservesParticipants() {
        User participant = user(3L);
        Teacher teacher = Teacher.builder().id(2L).build();
        Session existing = validSession().setId(1L).setUsers(new ArrayList<>(List.of(participant)));
        Session changes = validSession().setName("Updated");

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(teacherRepository.findById(2L)).thenReturn(Optional.of(teacher));
        when(sessionRepository.save(existing)).thenReturn(existing);

        Session updated = sessionService.update(1L, changes, 2L);

        assertEquals("Updated", updated.getName());
        assertEquals(List.of(participant), updated.getUsers());
        assertSame(teacher, updated.getTeacher());
    }

    @Test
    void participateRejectsDuplicateParticipation() {
        User user = user(3L);
        Session session = validSession().setId(1L).setUsers(new ArrayList<>(List.of(user)));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(3L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 3L));
    }

    @Test
    void noLongerParticipateRemovesUser() {
        User user = user(3L);
        Session session = validSession().setId(1L).setUsers(new ArrayList<>(List.of(user)));

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        sessionService.noLongerParticipate(1L, 3L);

        assertEquals(List.of(), session.getUsers());
        verify(sessionRepository).save(session);
    }

    private Session validSession() {
        return Session.builder()
                .name("Yoga")
                .date(new Date())
                .description("Session")
                .users(new ArrayList<>())
                .build();
    }

    private User user(Long id) {
        return User.builder()
                .id(id)
                .email("user%d@example.com".formatted(id))
                .firstName("First")
                .lastName("Last")
                .password("password")
                .admin(false)
                .build();
    }
}
