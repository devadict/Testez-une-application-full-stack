package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TeacherControllerTest {
    @Mock
    private TeacherMapper teacherMapper;
    @Mock
    private TeacherService teacherService;

    private TeacherController teacherController;

    @BeforeEach
    void setUp() {
        teacherController = new TeacherController(teacherService, teacherMapper);
    }

    @Test
    public void testFindByIdOk() {
        Long id = 1L;
        String firstname = "first";
        String lastname = "teacher";
        Teacher teacher = Teacher.builder().id(id).firstName(firstname).lastName(lastname).build();
        TeacherDto dto = new TeacherDto();
        dto.setId(id);
        dto.setFirstName(firstname);
        dto.setLastName(lastname);

        when(teacherService.findById(id)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(dto);

        ResponseEntity<?> response = teacherController.findById(""+id);
        TeacherDto responseBody = (TeacherDto) response.getBody();

        assertFindByIdResponse(HttpStatus.OK, dto, response);
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 1L;

        when(teacherService.findById(id)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById(""+id);

        assertFindByIdResponse(HttpStatus.NOT_FOUND, null, response);
    }

    @Test
    public void testFindByIdBadRequest() {
        ResponseEntity<?> response = teacherController.findById("abc");

        assertResponseStatus(HttpStatus.BAD_REQUEST, response);
    }

    @Test
    public void testFindAll() {
        List<Teacher> entities = new ArrayList<>();
        entities.add(Teacher.builder().id(1L).build());
        entities.add(Teacher.builder().id(2L).build());

        List<TeacherDto> dtos = new ArrayList<>();
        TeacherDto dto1 = new TeacherDto();
        dto1.setId(1L);
        TeacherDto dto2 = new TeacherDto();
        dto2.setId(2L);
        dtos.add(dto1);
        dtos.add(dto2);

        when(teacherService.findAll()).thenReturn(entities);
        when(teacherMapper.toDto(entities)).thenReturn(dtos);

        ResponseEntity<?> response = teacherController.findAll();
        List<TeacherDto> responseBody = (List<TeacherDto>) response.getBody();

        assertFindAllResponse(dtos, responseBody);
    }

    private void assertFindByIdResponse(HttpStatus expectedHttpStatus, TeacherDto expectedResponseBody, ResponseEntity<?> response) {
        assertResponseStatus(expectedHttpStatus, response);

        if (expectedHttpStatus == HttpStatus.OK) {
            TeacherDto responseBody = (TeacherDto) response.getBody();
            assertEquals(expectedResponseBody, responseBody);
        }
    }

    private void assertResponseStatus(HttpStatus expectedHttpStatus, ResponseEntity<?> response) {
        assertEquals(expectedHttpStatus, response.getStatusCode());
    }

    private void assertFindAllResponse(List<TeacherDto> expectedResponseBody, List<TeacherDto> responseBody) {
        assertEquals(expectedResponseBody, responseBody);
    }
}
