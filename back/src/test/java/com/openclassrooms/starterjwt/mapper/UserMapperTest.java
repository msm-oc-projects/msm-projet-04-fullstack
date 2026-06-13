package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void mapsUserBothWays() {
        User user = user();

        UserDto dto = mapper.toDto(user);
        User mappedBack = mapper.toEntity(dto);

        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getEmail(), mappedBack.getEmail());
        assertEquals(user.isAdmin(), mappedBack.isAdmin());
    }

    @Test
    void mapsListsAndNullValues() {
        User user = user();
        UserDto dto = mapper.toDto(user);

        assertEquals(1, mapper.toDto(List.of(user)).size());
        assertEquals(1, mapper.toEntity(List.of(dto)).size());
        assertNull(mapper.toDto((User) null));
        assertNull(mapper.toEntity((UserDto) null));
        assertNull(mapper.toDto((List<User>) null));
        assertNull(mapper.toEntity((List<UserDto>) null));
    }

    private User user() {
        return User.builder()
                .id(1L)
                .email("user@example.com")
                .firstName("First")
                .lastName("Last")
                .password("password")
                .admin(false)
                .build();
    }
}
