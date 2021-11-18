package com.javeriana.ares.apigateway.builder;

import com.javeriana.ares.apigateway.ApplicationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@ContextConfiguration(classes = {ApplicationTest.class})
@TestPropertySource(properties = {"SPRING_PROFILES_ACTIVE=test"})
public abstract class BuilderConfigurationTest {
}
