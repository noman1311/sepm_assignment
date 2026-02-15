package com.example.webapp.repository;

import com.example.webapp.entity.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TeacherRepositoryTest {

    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        teacherRepository = mock(TeacherRepository.class);
    }

    @Test
    void findByUsername_returnsTeacher() {
        Teacher teacher = new Teacher();
        teacher.setUsername("john");
        teacher.setPassword("pass");
        teacher.setName("John Doe");

        when(teacherRepository.findByUsername("john"))
                .thenReturn(teacher);

        Teacher found = teacherRepository.findByUsername("john");

        assertNotNull(found);
        assertEquals("John Doe", found.getName());

        verify(teacherRepository).findByUsername("john");
    }

    @Test
    void findByUsername_returnsNullIfNotFound() {
        when(teacherRepository.findByUsername("nonexistent"))
                .thenReturn(null);

        Teacher found = teacherRepository.findByUsername("nonexistent");

        assertNull(found);
        verify(teacherRepository).findByUsername("nonexistent");
    }
}
