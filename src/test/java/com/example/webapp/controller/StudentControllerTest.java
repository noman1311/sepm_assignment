package com.example.webapp.controller;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Course;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    private StudentService studentService;
    private CourseRepository courseRepository;
    private StudentController studentController;

    private HttpSession sessionWithTeacher;
    private HttpSession sessionWithStudent;
    private HttpSession emptySession;

    @BeforeEach
    void setUp() {
        studentService = mock(StudentService.class);
        courseRepository = mock(CourseRepository.class);

        studentController = new StudentController(studentService, courseRepository);

        sessionWithTeacher = mock(HttpSession.class);
        sessionWithStudent = mock(HttpSession.class);
        emptySession = mock(HttpSession.class);

        when(sessionWithTeacher.getAttribute("teacher")).thenReturn(new Object());
        when(sessionWithStudent.getAttribute("studentId")).thenReturn(1L);
        when(emptySession.getAttribute(anyString())).thenReturn(null);
    }

    @Test
    void getStudents_returnsStudentsView() {
        Model model = mock(Model.class);

        List<Student> students = Arrays.asList(new Student(), new Student());
        when(studentService.getAllStudents()).thenReturn(students);

        String view = studentController.getStudents(model);

        assertEquals("students", view);
        verify(model).addAttribute("students", students);
    }

    @Test
    void addStudent_withTeacher_returnsStudentForm() {
        Model model = mock(Model.class);
        List<Course> courses = Arrays.asList(new Course(), new Course());
        when(courseRepository.findAll()).thenReturn(courses);

        String view = studentController.addStudent(model, sessionWithTeacher);

        assertEquals("student-form", view);
        verify(model).addAttribute(eq("student"), any(StudentDTO.class));
        verify(model).addAttribute("courses", courses);
    }

    @Test
    void addStudent_withoutTeacher_redirectsToLogin() {
        Model model = mock(Model.class);

        String view = studentController.addStudent(model, emptySession);

        assertEquals("redirect:/teacher/login", view);
    }

    @Test
    void storeStudent_redirectsToStudents() {
        StudentDTO dto = new StudentDTO();
        String view = studentController.storeStudent(dto, mock(Model.class));

        assertEquals("redirect:/students", view);
        verify(studentService).saveStudent(dto);
    }

    @Test
    void login_validCredentials_setsSessionAndRedirects() {
        Student student = new Student();
        student.setId(1L);

        when(studentService.login(1L, "pass")).thenReturn(student);

        HttpSession session = mock(HttpSession.class);

        String view = studentController.login(1L, "pass", session);

        assertEquals("redirect:/students/profile", view);
        verify(session).setAttribute("studentId", 1L);
    }

    @Test
    void login_invalidCredentials_returnsLoginPage() {
        when(studentService.login(1L, "wrong")).thenReturn(null);

        String view = studentController.login(1L, "wrong", mock(HttpSession.class));

        assertEquals("student-login", view);
    }

    @Test
    void studentProfile_withValidSession_returnsProfileView() {
        Model model = mock(Model.class);

        Student student = new Student();
        when(studentService.getStudentWithCourses(1L)).thenReturn(student);

        String view = studentController.studentProfile(sessionWithStudent, model);

        assertEquals("student-profile", view);
        verify(model).addAttribute("student", student);
    }

    @Test
    void studentProfile_withoutSession_redirectsToLogin() {
        Model model = mock(Model.class);

        String view = studentController.studentProfile(emptySession, model);

        assertEquals("redirect:/students/login", view);
    }

    @Test
    void studentLoginPage_returnsLoginView() {
        String view = studentController.studentLoginPage();

        assertEquals("student-login", view);
    }

    @Test
    void updateProfile_withValidSession_updatesAndRedirects() {
        Student existing = new Student();
        when(studentService.getStudentWithCourses(1L)).thenReturn(existing);

        Student updated = new Student();

        String view = studentController.updateProfile(updated, sessionWithStudent);

        assertEquals("redirect:/students/profile", view);
        verify(studentService).updateProfile(updated, existing);
    }

    @Test
    void updateProfile_withoutSession_redirectsToLogin() {
        Student updated = new Student();

        String view = studentController.updateProfile(updated, emptySession);

        assertEquals("redirect:/students/login", view);
        verify(studentService, never()).updateProfile(any(), any());
    }
}
