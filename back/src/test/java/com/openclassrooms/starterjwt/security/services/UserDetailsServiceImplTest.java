package com.openclassrooms.starterjwt.security.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "test@example.com", "John", "Doe", "password", true, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void shouldLoadUserByUsername() {
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getEmail());


        assertAll(
            () -> assertThat(userDetails.getUsername()).isEqualTo(testUser.getEmail()),
            () -> assertThat(userDetails.getPassword()).isEqualTo(testUser.getPassword()),
            () -> verify(userRepository).findByEmail(testUser.getEmail())
        );
    }

    @Test
    void shouldThrowUsernameNotFoundException() {
      
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(testUser.getEmail()))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User Not Found with email: " + testUser.getEmail());
        
        verify(userRepository).findByEmail(testUser.getEmail());
    }
}
