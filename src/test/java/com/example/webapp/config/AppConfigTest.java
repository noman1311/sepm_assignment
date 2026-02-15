package com.example.webapp.config;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = AppConfig.class)
class AppConfigTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void modelMapperBeanIsCreated() {
        ModelMapper mapper = context.getBean(ModelMapper.class);
        assertThat(mapper).isNotNull();
    }
}