package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        authenticationService = new AuthenticationService(
                authenticationManager,
                jwtUtils,
                passwordEncoder,
                userRepository
        );
    }

    @Test
    void authenticateReturnsJwtAndStoresAuthentication() {
        LoginRequest request = new LoginRequest();
        request.setEmail("admin@example.com");
        request.setPassword("password");
        UserDetailsImpl details = UserDetailsImpl.builder()
                .id(1L)
                .username(request.getEmail())
                .firstName("Admin")
                .lastName("User")
                .admin(true)
                .password("encoded")
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                details,
                null,
                details.getAuthorities()
        );
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("jwt-token");

        JwtResponse response = authenticationService.authenticate(request);

        assertEquals("jwt-token", response.getToken());
        assertEquals("Bearer", response.getType());
        assertEquals(1L, response.getId());
        assertEquals(request.getEmail(), response.getUsername());
        assertTrue(response.getAdmin());
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void registerRejectsAnExistingEmail() {
        SignupRequest request = signupRequest();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authenticationService.register(request));
    }

    @Test
    void registerEncodesPasswordAndSavesUser() {
        SignupRequest request = signupRequest();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encoded");

        authenticationService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("encoded", userCaptor.getValue().getPassword());
        assertFalse(userCaptor.getValue().isAdmin());
    }

    private SignupRequest signupRequest() {
        SignupRequest request = new SignupRequest();
        request.setEmail("user@example.com");
        request.setFirstName("First");
        request.setLastName("Last");
        request.setPassword("password");
        return request;
    }
}
