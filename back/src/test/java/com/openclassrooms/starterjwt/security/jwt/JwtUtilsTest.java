package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtUtilsTest {

    private static final String SECRET =
            "cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e";

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", SECRET);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 60_000);
    }

    @Test
    void generatesAndReadsValidToken() {
        UserDetailsImpl details = UserDetailsImpl.builder()
                .id(1L)
                .username("user@example.com")
                .password("password")
                .admin(false)
                .build();
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());

        String token = jwtUtils.generateJwtToken(authentication);

        assertTrue(jwtUtils.validateJwtToken(token));
        assertEquals(details.getUsername(), jwtUtils.getUserNameFromJwtToken(token));
    }

    @Test
    void rejectsMalformedToken() {
        assertFalse(jwtUtils.validateJwtToken("not-a-jwt"));
    }

    @Test
    void rejectsExpiredToken() {
        String token = Jwts.builder()
                .setSubject("user@example.com")
                .setIssuedAt(new Date(System.currentTimeMillis() - 120_000))
                .setExpiration(new Date(System.currentTimeMillis() - 60_000))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void rejectsTokenWithInvalidSignature() {
        String otherSecret = SECRET.replace('c', 'd');
        String token = Jwts.builder()
                .setSubject("user@example.com")
                .setExpiration(new Date(System.currentTimeMillis() + 60_000))
                .signWith(SignatureAlgorithm.HS512, otherSecret)
                .compact();

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void rejectsEmptyToken() {
        assertFalse(jwtUtils.validateJwtToken(""));
    }
}
