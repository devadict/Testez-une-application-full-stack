package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)

public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Test
    public void getUserByIdTest() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserService userService = new UserService(userRepository);
        assertSame(user, userService.findById(1L));
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(userRepository).deleteById(1L);
        UserService userService = new UserService(userRepository);
        userService.delete(1L);
        verify(userRepository).deleteById(1L);
    }

}
