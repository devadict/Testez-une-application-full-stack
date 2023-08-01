package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    private TeacherService teacherService;

    @Test
    public void testFindAllTeachers(){
    
        List<Teacher> teachers = List.of(
                new Teacher(1L, "Alice", "Smith", LocalDateTime.now(), LocalDateTime.now()),
                new Teacher(2L, "John", "Wick", LocalDateTime.now(), LocalDateTime.now())
        );

        when(teacherRepository.findAll()).thenReturn(teachers);

        
        teacherService = new TeacherService(teacherRepository);
        List<Teacher> result = teacherService.findAll();

    
        verify(teacherRepository).findAll();
        assertThat(result).isEqualTo(teachers);
    }

    @Test
    public void testFindByTeacherId(){
        
        Long id = 1L;
        Teacher teacher = new Teacher(id, "Alice", "Smith", LocalDateTime.now(), LocalDateTime.now());

        when(teacherRepository.findById(id)).thenReturn(Optional.of(teacher));

        
        teacherService = new TeacherService(teacherRepository);
        Teacher result = teacherService.findById(id);

    
        verify(teacherRepository).findById(id);
        assertThat(result).isEqualTo(teacher);
    }

}
