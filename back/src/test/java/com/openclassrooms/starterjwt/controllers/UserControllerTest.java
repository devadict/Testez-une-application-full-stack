package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserControllerTest {
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserService userService;

    @Test
    public void findByIdTest() {
        Long id = 1L;
        User user = User.builder()
                .id(id)
                .email("new@user.com")
                .firstName("Alice")
                .lastName("Smith")
                .password("newpass")
                .admin(false).build();

        UserDto dto = new UserDto();
        dto.setId(id);
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPassword(user.getPassword());
        dto.setAdmin(user.isAdmin());

        when(userService.findById(id)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(dto);

        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.findById(id.toString());
        UserDto responseBody = (UserDto) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dto, responseBody);
    }

    @Test
    public void findByIdNotFoundTest() {
        Long id = 1L;

        when(userService.findById(id)).thenReturn(null);

        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.findById(id.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void findByIdBadRequestTest() {
        UserController userController = new UserController(userService, userMapper);
        ResponseEntity<?> response = userController.findById("");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteTest() {
        Long id = 1L;
        User user = User.builder()
                .id(id)
                .email("new@user.com")
                .firstName("Alice")
                .lastName("Smith")
                .password("newpass")
                .admin(false).build();

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(id)
                .username(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .admin(user.isAdmin()).build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(userService.findById(id)).thenReturn(user);

        
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        
        when(securityContext.getAuthentication()).thenReturn(authentication);
        
        doNothing().when(userService).delete(id);

        UserController userController = new UserController(userService, userMapper);
        
        ResponseEntity<?> response = userController.delete(id.toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void deleteNotFoundTest() {
        Long id = 1L;

        when(userService.findById(id)).thenReturn(null);

        UserController userController = new UserController(userService, userMapper);
        
        ResponseEntity<?> response = userController.delete(id.toString());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteBadRequestTest() {
        
        UserController userController = new UserController(userService, userMapper);
        
        ResponseEntity<?> response = userController.delete("");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}

