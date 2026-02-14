package com.example.webapp.repository;

import com.example.webapp.entity.Department;

import org.springframework.data.jpa.repository.JpaRepository;



public interface DepartmentRepository extends JpaRepository<Department, Long>
{}
