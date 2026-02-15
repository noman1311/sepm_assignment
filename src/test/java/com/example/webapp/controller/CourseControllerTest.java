package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.repository.CourseRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    private CourseRepository courseRepository;
    private CourseController courseController;

    private HttpSession sessionWithTeacher;
    private HttpSession sessionWithoutTeacher;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        courseController = new CourseController(courseRepository);

        sessionWithTeacher = mock(HttpSession.class);
        sessionWithoutTeacher = mock(HttpSession.class);

        when(sessionWithTeacher.getAttribute("teacher")).thenReturn(new Object());
        when(sessionWithoutTeacher.getAttribute("teacher")).thenReturn(null);
    }

    @Test
    void addCourse_withTeacher_returnsCourseForm() {
        Model model = mock(Model.class);

        String view = courseController.addCourse(model, sessionWithTeacher);

        assertEquals("course-form", view);
        verify(model, times(1)).addAttribute(eq("course"), any(Course.class));
    }

    @Test
    void addCourse_withoutTeacher_redirectsToLogin() {
        Model model = mock(Model.class);

        String view = courseController.addCourse(model, sessionWithoutTeacher);

        assertEquals("redirect:/teacher/login", view);
        verify(model, never()).addAttribute(anyString(), any());
    }

    @Test
    void storeCourse_withTeacher_savesAndRedirects() {
        Course course = new Course();

        String view = courseController.storeCourse(course, sessionWithTeacher);

        assertEquals("redirect:/teacher/dashboard", view);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void storeCourse_withoutTeacher_redirectsToLogin() {
        Course course = new Course();

        String view = courseController.storeCourse(course, sessionWithoutTeacher);

        assertEquals("redirect:/teacher/login", view);
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void listCourses_withTeacher_returnsCoursesView() {
        Model model = mock(Model.class);

        List<Course> courses = Arrays.asList(new Course(), new Course());
        when(courseRepository.findAll()).thenReturn(courses);

        String view = courseController.listCourses(model, sessionWithTeacher);

        assertEquals("courses", view);
        verify(model, times(1)).addAttribute("courses", courses);
    }

    @Test
    void listCourses_withoutTeacher_redirectsToLogin() {
        Model model = mock(Model.class);

        String view = courseController.listCourses(model, sessionWithoutTeacher);

        assertEquals("redirect:/teacher/login", view);
        verify(model, never()).addAttribute(anyString(), any());
    }
}
