package com.example.webapp.repository;

import com.example.webapp.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StudentRepositoryTest {

    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
    }

    @Test
    void saveAndFindById() {
        Student student = new Student();
        student.setId(100L);
        student.setName("Test Student");

        when(studentRepository.findById(100L))
                .thenReturn(Optional.of(student));

        Optional<Student> found = studentRepository.findById(100L);

        assertTrue(found.isPresent());
        assertEquals("Test Student", found.get().getName());

        verify(studentRepository).findById(100L);
    }

    @Test
    void findAll_returnsAllStudents() {
        Student s1 = new Student();
        s1.setId(1L);

        Student s2 = new Student();
        s2.setId(2L);

        when(studentRepository.findAll())
                .thenReturn(Arrays.asList(s1, s2));

        List<Student> students = studentRepository.findAll();

        assertEquals(2, students.size());
        verify(studentRepository).findAll();
    }

    @Test
    void existsById_returnsTrueIfExists() {
        when(studentRepository.existsById(10L))
                .thenReturn(true);

        boolean exists = studentRepository.existsById(10L);

        assertTrue(exists);
        verify(studentRepository).existsById(10L);
    }
}
