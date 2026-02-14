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

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getStudents(Model model) {
        model.addAttribute("students", studentService.getAllStudents());
        return "students";
    }

    @GetMapping("/add")
    public String addStudent(Model model) {
        model.addAttribute("student", new StudentDTO());
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
            session.setAttribute("student", student);
            return "redirect:/students/profile";
        }
        return "student-login";
    }
    @GetMapping("/profile")
    public String studentProfile(Model model) {

        // TEMPORARY object for now (later this comes from session)
        Student student = new Student();
//        student.setId(101L);
//        student.setName("Test Student");
//        student.setHometown("Dhaka");
//        student.setSemester(5);
//        student.setYear(2026);

        model.addAttribute("student", student);

        return "student-profile";
    }
    @GetMapping("/login")
    public String studentLoginPage() {
        return "student-login";
    }


}

