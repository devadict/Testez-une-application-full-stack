package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.services.SessionService;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionService;

    @Test
    public void shouldReturnSessionDtoWhenIdExists() {
    
      Long id = 2L;
      Session session = Session.builder().id(id).name("Test session").build();
      SessionDto sessionDto = new SessionDto();
      sessionDto.setId(id);
      sessionDto.setName(session.getName());
    
      when(sessionService.getById(id)).thenReturn(session);
      when(sessionMapper.toDto(session)).thenReturn(sessionDto);
    
      SessionController controller = new SessionController(sessionService, sessionMapper);
      ResponseEntity<?> response = controller.findById(id.toString());
    
      verify(sessionService).getById(id);
      verify(sessionMapper).toDto(session);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertEquals(response.getBody(), sessionDto);
    
    }
    
    @Test
    public void shouldReturnNotFoundWhenIdDoesNotExist() {
    
      Long id = 3L;
    
      when(sessionService.getById(id)).thenReturn(null);
    
      SessionController controller = new SessionController(sessionService, sessionMapper);
      ResponseEntity<?> response = controller.findById(id.toString());
    
      verify(sessionService).getById(id);
      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
      assertNull(response.getBody());
    
    }
    
    @Test
    public void shouldReturnBadRequestWhenIdIsInvalid() {
    
      SessionController controller = new SessionController(sessionService, sessionMapper);
      ResponseEntity<?> response = controller.findById("invalid");
    
      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    
    }
    
    @Test
    public void shouldReturnAllSessions() {

        List<Session> sessions = List.of(new Session(), new Session(), new Session());
        List<SessionDto> sessionDtos = List.of(new SessionDto(), new SessionDto(), new SessionDto());

        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        SessionController controller = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = controller.findAll();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sessionDtos, response.getBody());

    }
    
    @Test
    public void testCreateOk() {
        String name = "hello world";
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName(name);
        Session session = Session.builder().name(name).build();

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.create(session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.create(sessionDto);
        SessionDto responseBody = (SessionDto) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, responseBody);
    }

    @Test
    public void testUpdateOk() {
        Long id = 1L;
        String name = "hello world";
        SessionDto sessionDto = new SessionDto();
        sessionDto.setName(name);
        Session session = Session.builder().name(name).build();

        when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
        when(sessionService.update(id, session)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.update(""+id, sessionDto);
        SessionDto responseBody = (SessionDto) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, responseBody);
    }

    @Test
    public void testUpdateBadRequest() {
        SessionController sessionController = new SessionController(sessionService, sessionMapper);
        ResponseEntity<?> response = sessionController.update("abc", new SessionDto());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
public void createSessionTest() {

    String name = "new session";
    SessionDto sessionDto = new SessionDto();
    sessionDto.setName(name);
    Session session = Session.builder().name(name).build();

    when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
    when(sessionService.create(session)).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> response = sessionController.create(sessionDto);
    SessionDto responseBody = (SessionDto) response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(sessionDto, responseBody);
}

@Test
public void updateSessionTestOK() {

    Long id = 1L;
    String name = "update session test";
    SessionDto sessionDto = new SessionDto();
    sessionDto.setName(name);
    Session session = Session.builder().name(name).build();

    when(sessionMapper.toEntity(sessionDto)).thenReturn(session);
    when(sessionService.update(id, session)).thenReturn(session);
    when(sessionMapper.toDto(session)).thenReturn(sessionDto);

    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> response = sessionController.update(""+id, sessionDto);
    SessionDto responseBody = (SessionDto) response.getBody();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(sessionDto, responseBody);
}

@Test
public void updateSessionTestBadRequest() {

    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> response = sessionController.update("invalidId", null);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

}

@Test
public void deleteSessionTestOK() {
    Long id = 1L;
    when(sessionService.getById(id)).thenReturn(new Session());
    doNothing().when(sessionService).delete(id);

    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> responseEntity = sessionController.delete(""+id);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
}

@Test
public void deleteSessionTestNotFound() {
    Long id = 1L;
    when(sessionService.getById(id)).thenReturn(null);

    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> responseEntity = sessionController.delete(""+id);

    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
}

@Test
public void deleteSessionTestBadRequest() {
    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> responseEntity = sessionController.delete("invalidId");

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
}

@Test
public void participateSessionTestOK() {
    Long id = 1L;
    Long userId = 2L;

    doNothing().when(sessionService).participate(id, userId);

    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> responseEntity = sessionController.participate(""+id, ""+userId);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
}

@Test
public void participateSessionTestBadRequest() {
    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> responseEntity = sessionController.participate("invalidId", "invalidUserId");

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
}

@Test
public void noLongerParticipateSessionTestOK() {
    Long id = 1L;
    Long userId = 2L;

    doNothing().when(sessionService).noLongerParticipate(id, userId);

    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(""+id, ""+userId);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
}

@Test
public void noLongerParticipateSessionTestBadRequest() {
    SessionController sessionController = new SessionController(sessionService, sessionMapper);
    ResponseEntity<?> responseEntity = sessionController.noLongerParticipate("invalidId", "invalidUserId");

    assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
}


    
}

