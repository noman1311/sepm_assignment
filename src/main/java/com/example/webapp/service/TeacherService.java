package com.example.webapp.service;

import com.example.webapp.entity.Teacher;
import com.example.webapp.repository.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class TeacherService {

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher login(String username, String password) {
        Teacher teacher = teacherRepository.findByUsername(username);
        if (teacher != null && teacher.getPassword().equals(password)) {
            return teacher;
        }
        return null;
    }
}
