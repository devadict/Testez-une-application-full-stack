package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    public void testGenerateJwtToken() {
        // Generate a JWT token using a mock authentication object
        String token = jwtUtils.generateJwtToken(mockAuthentication("testuser", "testpassword"));

        // Assert that the token is not null or empty, and can be parsed and validated
        assertAll(
            () -> assertFalse(token.isEmpty()),
            () -> assertEquals("testuser", jwtUtils.getUserNameFromJwtToken(token)),
            () -> assertTrue(jwtUtils.validateJwtToken(token))
        );
    }

    @Test
    public void testMalformedJwt() {
        assertFalse(jwtUtils.validateJwtToken("not-a-valid-jwt"));
    }

    @Test
    public void testInvalidSignatureJwt() {
        // Generate a JWT token with a wrong secret
        String jwt = generateJwt("test@email.com", "wrongSecret");

        assertFalse(jwtUtils.validateJwtToken(jwt));
    }

    @Test
    public void testExpiredJwt() {
        // Generate a JWT token with an expired date
        String jwt = generateJwt("test@email.com", "testSecret", -1);

        assertFalse(jwtUtils.validateJwtToken(jwt));
    }

    // Helper method to create a mock authentication object
    private Authentication mockAuthentication(String username, String password) {
        UserDetails userDetails = new UserDetailsImpl(null, username, password, null, null, null);
        return new UsernamePasswordAuthenticationToken(userDetails, null);
    }

    // Helper method to generate a JWT token with a given subject and secret
    private String generateJwt(String subject, String secret) {
        return generateJwt(subject, secret, 1000);
    }

    // Helper method to generate a JWT token with a given subject, secret and expiration time
    private String generateJwt(String subject, String secret, long expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
