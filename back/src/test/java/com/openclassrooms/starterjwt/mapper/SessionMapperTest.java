package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SessionMapperTest {

    private final SessionMapper mapper = Mappers.getMapper(SessionMapper.class);

    @Test
    void mapsDtoToEntityWithoutRelationships() {
        SessionDto dto = new SessionDto(
                1L,
                "Yoga",
                new Date(),
                2L,
                "Description",
                List.of(3L),
                null,
                null
        );

        Session session = mapper.toEntity(dto);

        assertEquals(dto.getId(), session.getId());
        assertEquals(dto.getName(), session.getName());
        assertEquals(dto.getDate(), session.getDate());
        assertEquals(dto.getDescription(), session.getDescription());
        assertNull(session.getTeacher());
        assertNull(session.getUsers());
    }

    @Test
    void mapsEntityToDtoWithTeacherAndParticipants() {
        Teacher teacher = Teacher.builder().id(2L).firstName("First").lastName("Teacher").build();
        User user = user(3L);
        Session session = Session.builder()
                .id(1L)
                .name("Yoga")
                .date(new Date())
                .description("Description")
                .teacher(teacher)
                .users(List.of(user))
                .build();

        SessionDto dto = mapper.toDto(session);

        assertEquals(teacher.getId(), dto.getTeacher_id());
        assertEquals(List.of(user.getId()), dto.getUsers());
    }

    @Test
    void mapsMissingParticipantsToEmptyList() {
        Session session = Session.builder()
                .id(1L)
                .name("Yoga")
                .date(new Date())
                .description("Description")
                .teacher(Teacher.builder().id(2L).build())
                .users(null)
                .build();

        assertEquals(List.of(), mapper.toDto(session).getUsers());
    }

    @Test
    void mapsListsAndNullValues() {
        SessionDto dto = new SessionDto(
                1L,
                "Yoga",
                new Date(),
                2L,
                "Description",
                List.of(),
                null,
                null
        );

        assertEquals(1, mapper.toEntity(List.of(dto)).size());
        assertEquals(1, mapper.toDto(List.of(entity())).size());
        assertNull(mapper.toEntity((SessionDto) null));
        assertNull(mapper.toDto((Session) null));
        assertNull(mapper.toEntity((List<SessionDto>) null));
        assertNull(mapper.toDto((List<Session>) null));
    }

    private Session entity() {
        return Session.builder()
                .id(1L)
                .name("Yoga")
                .date(new Date())
                .description("Description")
                .teacher(Teacher.builder().id(2L).build())
                .users(List.of())
                .build();
    }

    private User user(Long id) {
        return User.builder()
                .id(id)
                .email("user@example.com")
                .firstName("First")
                .lastName("Last")
                .password("password")
                .admin(false)
                .build();
    }
}
