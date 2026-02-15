package com.example.webapp.service;

import com.example.webapp.entity.Teacher;
import com.example.webapp.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    void login_withValidCredentials_returnsTeacher() {
        Teacher teacher = new Teacher();
        teacher.setUsername("admin");
        teacher.setPassword("secret");
        when(teacherRepository.findByUsername("admin")).thenReturn(teacher);

        Teacher result = teacherService.login("admin", "secret");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("admin");
    }

    @Test
    void login_withInvalidPassword_returnsNull() {
        Teacher teacher = new Teacher();
        teacher.setUsername("admin");
        teacher.setPassword("secret");
        when(teacherRepository.findByUsername("admin")).thenReturn(teacher);

        Teacher result = teacherService.login("admin", "wrong");

        assertThat(result).isNull();
    }

    @Test
    void login_withNonExistingUsername_returnsNull() {
        when(teacherRepository.findByUsername("unknown")).thenReturn(null);

        Teacher result = teacherService.login("unknown", "pass");

        assertThat(result).isNull();
    }
}