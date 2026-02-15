package com.example.webapp.controller;

import com.example.webapp.entity.Course;
import com.example.webapp.repository.CourseRepository;
import com.example.webapp.service.TeacherService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping("/add")
    public String addCourse(Model model, HttpSession session) {

        // Optional: protect page (teacher only)
        Object teacher = session.getAttribute("teacher");
        if (teacher == null) {
            return "redirect:/teacher/login";
        }

        model.addAttribute("course", new Course());
        return "course-form";
    }
    @PostMapping("/store")
    public String storeCourse(@ModelAttribute Course course, HttpSession session) {

        if (session.getAttribute("teacher") == null) {
            return "redirect:/teacher/login";
        }

        courseRepository.save(course);
        return "redirect:/teacher/dashboard";
    }
    @GetMapping
    public String listCourses(Model model, HttpSession session) {

        if (session.getAttribute("teacher") == null) {
            return "redirect:/teacher/login";
        }

        model.addAttribute("courses", courseRepository.findAll());
        return "courses";
    }

}
