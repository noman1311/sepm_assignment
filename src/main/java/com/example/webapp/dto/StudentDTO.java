package com.example.webapp.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

//@Data
public class StudentDTO {

    private Long id;        // teacher enters this
    private String name;
    private String password;


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
}
