package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.exception.UnauthorizedException;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void findByIdReturnsUser() {
        User user = user("owner@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertSame(user, userService.findById(1L));
    }

    @Test
    void findByIdThrowsWhenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.findById(1L));
    }

    @Test
    void deleteRejectsAnotherUsersAccount() {
        User user = user("owner@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(
                UnauthorizedException.class,
                () -> userService.delete(1L, "other@example.com")
        );
        verify(userRepository, never()).delete(user);
    }

    @Test
    void deleteRemovesAuthenticatedUsersAccount() {
        User user = user("owner@example.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L, "owner@example.com");

        verify(userRepository).delete(user);
    }

    private User user(String email) {
        return User.builder()
                .id(1L)
                .email(email)
                .firstName("First")
                .lastName("Last")
                .password("password")
                .admin(false)
                .build();
    }
}
