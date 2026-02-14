package com.example.webapp.repository;

import com.example.webapp.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Teacher findByUsername(String username);
}
