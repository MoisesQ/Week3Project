package com.projects.springboot.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.projects.springboot.app.config.ConfigProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;


@WebFluxTest(value = ConfigProperties.class)
@ExtendWith(MockitoExtension.class)
public class ConfigPropertiesTest {

  @Autowired
  private ConfigProperties configProperties;

  @Test
  void testUserProperty() {
    assertEquals(configProperties.getUser(),"user");
  }

  @Test
  void testPasswordProperty() {
    assertEquals(configProperties.getPassword(), "user");
  }
}
