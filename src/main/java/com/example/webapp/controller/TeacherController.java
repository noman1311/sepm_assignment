package com.example.webapp.controller;

import com.example.webapp.entity.Teacher;
import com.example.webapp.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "teacher-login";
    }

    @PostMapping("/login")
    public String login(String username, String password, HttpSession session) {
        Teacher teacher = teacherService.login(username, password);
        if (teacher != null) {
            session.setAttribute("teacher", teacher);
            return "redirect:/teacher/dashboard";
        }
        return "teacher-login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, org.springframework.ui.Model model) {

        Teacher teacher = (Teacher) session.getAttribute("teacher");

        if (teacher == null) {
            return "redirect:/teacher/login";
        }

        model.addAttribute("teacher", teacher);
        return "teacher-dashboard";
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // clears teacher + student session
        return "redirect:/";
    }


}
