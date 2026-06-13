package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Test
    void loadUserByUsernameCopiesUserData() {
        User user = User.builder()
                .id(1L)
                .email("admin@example.com")
                .firstName("Admin")
                .lastName("User")
                .password("password")
                .admin(true)
                .build();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetailsImpl details = (UserDetailsImpl) new UserDetailsServiceImpl(userRepository)
                .loadUserByUsername(user.getEmail());

        assertEquals(user.getId(), details.getId());
        assertEquals(user.getEmail(), details.getUsername());
        assertEquals(user.getFirstName(), details.getFirstName());
        assertEquals(user.getLastName(), details.getLastName());
        assertEquals(user.getPassword(), details.getPassword());
        assertTrue(details.getAdmin());
    }

    @Test
    void loadUserByUsernameRejectsUnknownUser() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> new UserDetailsServiceImpl(userRepository).loadUserByUsername("unknown@example.com")
        );
    }
}
