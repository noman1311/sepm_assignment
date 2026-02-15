package com.example.webapp.service;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Course;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentDTO studentDTO;
    private Course course1, course2;

    @BeforeEach
    void setUp() {
        student = new Student();
        student.setId(1L);
        student.setName("John");
        student.setPassword("pass");

        studentDTO = new StudentDTO();
        studentDTO.setId(1L);
        studentDTO.setName("John");
        studentDTO.setPassword("pass");
        studentDTO.setCourseIds(Arrays.asList(101L, 102L));

        course1 = new Course();
        course1.setId(101L);
        course2 = new Course();
        course2.setId(102L);
    }

    @Test
    void getAllStudents_returnsAll() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList(student));

        List<Student> result = studentService.getAllStudents();

        assertThat(result).hasSize(1);
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById_returnsOptional() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Optional<Student> result = studentService.getStudentById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
    }

    @Test
    void saveStudent_withNewId_savesSuccessfully() {
        when(studentRepository.existsById(1L)).thenReturn(false);
        when(modelMapper.map(studentDTO, Student.class)).thenReturn(student);
        when(courseRepository.findAllById(Arrays.asList(101L, 102L))).thenReturn(Arrays.asList(course1, course2));
        when(studentRepository.save(student)).thenReturn(student);

        Student saved = studentService.saveStudent(studentDTO);

        assertThat(saved).isNotNull();
        verify(studentRepository).existsById(1L);
        verify(modelMapper).map(studentDTO, Student.class);
        verify(courseRepository).findAllById(Arrays.asList(101L, 102L));
        verify(studentRepository).save(student);
        assertThat(student.getCourses()).containsExactly(course1, course2);
    }

    @Test
    void saveStudent_withExistingId_throwsException() {
        when(studentRepository.existsById(1L)).thenReturn(true);

        assertThrows(RuntimeException.class, () -> studentService.saveStudent(studentDTO));

        verify(studentRepository, never()).save(any());
    }

    @Test
    void login_withValidCredentials_returnsStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.login(1L, "pass");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void login_withInvalidPassword_returnsNull() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.login(1L, "wrong");

        assertThat(result).isNull();
    }

    @Test
    void login_withNonExistingId_returnsNull() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        Student result = studentService.login(99L, "pass");

        assertThat(result).isNull();
    }

    @Test
    void updateProfile_updatesFieldsAndSaves() {
        Student updated = new Student();
        updated.setName("New Name");
        updated.setHometown("New Town");
        updated.setSemester(5);
        updated.setYear(2024);

        studentService.updateProfile(updated, student);

        assertThat(student.getName()).isEqualTo("New Name");
        assertThat(student.getHometown()).isEqualTo("New Town");
        assertThat(student.getSemester()).isEqualTo(5);
        assertThat(student.getYear()).isEqualTo(2024);

        verify(studentRepository).save(student);
    }

    @Test
    void getStudentWithCourses_returnsStudent() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentWithCourses(1L);

        assertThat(result).isEqualTo(student);
    }

    @Test
    void getStudentWithCourses_notFound_throwsException() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> studentService.getStudentWithCourses(1L));
    }
}