package com.api.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class)
public class CucumberSpringConfig {
}