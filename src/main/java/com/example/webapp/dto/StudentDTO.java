package com.example.webapp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@Data
public class StudentDTO {

    private Long id;        // teacher enters this
    private String name;
    private String password;
    private List<Long> courseIds;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public List<Long> getCourseIds() {
        return courseIds;
    }

    public void setCourseIds(List<Long> courseIds) {
        this.courseIds = courseIds;
    }
}
