package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDetailsImplTest {

    @Test
    void accountFlagsAreEnabledAndAuthoritiesAreEmpty() {
        UserDetailsImpl details = details(1L);

        assertTrue(details.isAccountNonExpired());
        assertTrue(details.isAccountNonLocked());
        assertTrue(details.isCredentialsNonExpired());
        assertTrue(details.isEnabled());
        assertTrue(details.getAuthorities().isEmpty());
    }

    @Test
    void equalityUsesTheUserIdentifier() {
        UserDetailsImpl details = details(1L);

        assertEquals(details, details);
        assertEquals(details, details(1L));
        assertNotEquals(details, details(2L));
        assertNotEquals(details, null);
        assertFalse(details.equals("user"));
    }

    private UserDetailsImpl details(Long id) {
        return UserDetailsImpl.builder()
                .id(id)
                .username("user@example.com")
                .firstName("First")
                .lastName("Last")
                .admin(false)
                .password("password")
                .build();
    }
}
