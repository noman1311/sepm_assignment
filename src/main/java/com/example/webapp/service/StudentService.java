package com.example.webapp.service;

import com.example.webapp.dto.StudentDTO;
import com.example.webapp.entity.Student;
import com.example.webapp.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    public StudentService(StudentRepository studentRepository, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student saveStudent(StudentDTO studentDTO) {
        if (studentRepository.existsById(studentDTO.getId())) {
            throw new RuntimeException("Student ID already exists");
        }
        Student student = modelMapper.map(studentDTO, Student.class);
        return studentRepository.save(student);
    }
    public Student login(Long id, String password) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            if (student.getPassword().equals(password)) {
                return student;
            }
        }
        return null;
    }
    public void updateProfile(Student updated, Student existing) {
        existing.setName(updated.getName());
        existing.setHometown(updated.getHometown());
        existing.setSemester(updated.getSemester());
        existing.setYear(updated.getYear());

        // roll, courses NOT updated
        studentRepository.save(existing);
    }



}
