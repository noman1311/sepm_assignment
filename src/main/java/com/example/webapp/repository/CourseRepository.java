package com.example.webapp.repository;

import com.example.webapp.entity.Course;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseRepository extends JpaRepository<Course, Long>
{}
