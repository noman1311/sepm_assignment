package com.example.webapp.controller;

import com.example.webapp.entity.Teacher;
import com.example.webapp.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TeacherControllerTest {

    private TeacherService teacherService;
    private TeacherController teacherController;

    private HttpSession sessionWithTeacher;
    private HttpSession emptySession;

    @BeforeEach
    void setUp() {
        teacherService = mock(TeacherService.class);
        teacherController = new TeacherController(teacherService);

        sessionWithTeacher = mock(HttpSession.class);
        emptySession = mock(HttpSession.class);

        when(sessionWithTeacher.getAttribute("teacher")).thenReturn(new Teacher());
        when(emptySession.getAttribute(anyString())).thenReturn(null);
    }

    @Test
    void loginPage_returnsLoginView() {
        String view = teacherController.loginPage();

        assertEquals("teacher-login", view);
    }

    @Test
    void login_validCredentials_setsSessionAndRedirectsToDashboard() {
        Teacher teacher = new Teacher();
        teacher.setUsername("admin");

        when(teacherService.login("admin", "pass")).thenReturn(teacher);

        HttpSession session = mock(HttpSession.class);

        String view = teacherController.login("admin", "pass", session);

        assertEquals("redirect:/teacher/dashboard", view);
        verify(session).setAttribute("teacher", teacher);
    }

    @Test
    void login_invalidCredentials_returnsLoginPage() {
        when(teacherService.login("admin", "wrong")).thenReturn(null);

        String view = teacherController.login("admin", "wrong", mock(HttpSession.class));

        assertEquals("teacher-login", view);
    }

    @Test
    void dashboard_withValidSession_returnsDashboardView() {
        Model model = mock(Model.class);

        Teacher teacher = new Teacher();
        teacher.setName("Mr. Smith");

        when(sessionWithTeacher.getAttribute("teacher")).thenReturn(teacher);

        String view = teacherController.dashboard(sessionWithTeacher, model);

        assertEquals("teacher-dashboard", view);
        verify(model).addAttribute("teacher", teacher);
    }

    @Test
    void dashboard_withoutSession_redirectsToLogin() {
        Model model = mock(Model.class);

        String view = teacherController.dashboard(emptySession, model);

        assertEquals("redirect:/teacher/login", view);
    }

    @Test
    void logout_invalidatesSessionAndRedirectsToHome() {
        HttpSession session = mock(HttpSession.class);

        String view = teacherController.logout(session);

        assertEquals("redirect:/", view);
        verify(session).invalidate();
    }
}
