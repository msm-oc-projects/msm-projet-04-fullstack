package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TeacherMapperTest {

    private final TeacherMapper mapper = Mappers.getMapper(TeacherMapper.class);

    @Test
    void mapsTeacherBothWays() {
        Teacher teacher = Teacher.builder().id(1L).firstName("First").lastName("Last").build();

        TeacherDto dto = mapper.toDto(teacher);
        Teacher mappedBack = mapper.toEntity(dto);

        assertEquals(teacher.getId(), dto.getId());
        assertEquals(teacher.getFirstName(), mappedBack.getFirstName());
        assertEquals(teacher.getLastName(), mappedBack.getLastName());
    }

    @Test
    void mapsListsAndNullValues() {
        Teacher teacher = Teacher.builder().id(1L).firstName("First").lastName("Last").build();
        TeacherDto dto = new TeacherDto(1L, "Last", "First", null, null);

        assertEquals(1, mapper.toDto(List.of(teacher)).size());
        assertEquals(1, mapper.toEntity(List.of(dto)).size());
        assertNull(mapper.toDto((Teacher) null));
        assertNull(mapper.toEntity((TeacherDto) null));
        assertNull(mapper.toDto((List<Teacher>) null));
        assertNull(mapper.toEntity((List<TeacherDto>) null));
    }
}
