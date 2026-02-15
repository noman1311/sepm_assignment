package com.example.webapp.controller;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Student;
import com.example.webapp.service.StudentService;
import jakarta.servlet.http.HttpSession;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.example.webapp.repository.CourseRepository;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final CourseRepository courseRepository;

    public StudentController(StudentService studentService,
                             CourseRepository courseRepository) {
        this.studentService = studentService;
        this.courseRepository = courseRepository;
    }


    @GetMapping
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/add")
    public String addStudent(Model model, HttpSession session) {

        if (session.getAttribute("teacher") == null) {
            return "redirect:/teacher/login";
        }

        model.addAttribute("student", new StudentDTO());
        model.addAttribute("courses", courseRepository.findAll()); // ADD THIS

        return "student-form";
    }


    @PostMapping("/store")
    public String storeStudent(@ModelAttribute("student") StudentDTO studentDTO, Model model) {
        studentService.saveStudent(studentDTO);
        return "redirect:/students";
    }

    @PostMapping("/login")
    public String login(@RequestParam Long id,
                        @RequestParam String password,
                        HttpSession session) {

        Student student = studentService.login(id, password);

        if (student != null) {
            session.setAttribute("studentId", student.getId()); // STORE ONLY ID
            return "redirect:/students/profile";
        }

        return "student-login";
    }

    @GetMapping("/profile")
    public String studentProfile(HttpSession session, Model model) {

        Long studentId = (Long) session.getAttribute("studentId");

        if (studentId == null) {
            return "redirect:/students/login";
        }

        Student student = studentService.getStudentWithCourses(studentId);

        model.addAttribute("student", student);
        return "student-profile";
    }

    @GetMapping("/login")
    public String studentLoginPage() {
        return "student-login";
    }
    @PostMapping("/update")
    public String updateProfile(@ModelAttribute Student updatedStudent,
                                HttpSession session) {

        Long studentId = (Long) session.getAttribute("studentId");

        if (studentId == null) {
            return "redirect:/students/login";
        }

        Student existing = studentService.getStudentWithCourses(studentId);

        studentService.updateProfile(updatedStudent, existing);

        return "redirect:/students/profile";
    }


}

