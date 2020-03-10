package com.projects.springboot.app;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.projects.springboot.app.controllers.StudentController;
import com.projects.springboot.app.entity.Student;
import com.projects.springboot.app.service.StudentService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(value = StudentController.class)
@ExtendWith(MockitoExtension.class)
public class BasicAuthTest {

  @MockBean
  private StudentService studentService;

  @Autowired
  private WebTestClient webTestClient;

  private Student stTest;

  /**
   * This method is created because we need to start with a student Object to use
   * in all tests.
   * 
   * @throws ParseException Using this exception because the date string format
   *                        doesn't work in the default constructor
   */
  @BeforeEach
  public void beforeAll() throws ParseException {
    stTest = new Student("M", "Alejandro", "Mateo", "Gonzales", 
        new SimpleDateFormat("yyyy-mm-dd").parse("1994-06-06"),
        "Nice Student");
  }

  @Test
  @WithMockUser
  public void createStudentAuthorizedTest() throws Exception {

    when(studentService.create(stTest)).thenReturn(Mono.just(stTest));

    webTestClient.mutateWith(csrf()).post().uri("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(stTest), Student.class)
            .exchange()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectStatus().isOk();
  }

  @Test
  public void createStudentUnauthorizedTest() throws Exception {

    when(studentService.create(stTest)).thenReturn(Mono.just(stTest));

    webTestClient.mutateWith(csrf()).post().uri("/api/students")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(stTest),Student.class)
            .exchange()
            .expectStatus().isUnauthorized();
  }
}
