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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@WebFluxTest(value = StudentController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser
public class StudentTest {

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private StudentService studentService;
  
  private Student stTest;
  private Student stTest2;

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
    stTest2 =  new Student("M", "Mario", "Jouse", "Gonzales", 
        new SimpleDateFormat("yyyy-mm-dd").parse("1996-08-16"),
        "Nice Student");
   
  }
  
  
  @Test

  public void createStudentIsOkTest() {

    when(studentService.create(stTest)).thenReturn(Mono.just(stTest));

    webClient.mutateWith(csrf()).post().uri("/api/students")
        .contentType(MediaType.APPLICATION_JSON).body(Mono.just(stTest), Student.class)
        .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk();

  }

  @Test
  public void findOneStudentIsOkTest() throws Exception {

    when(studentService.findById("5L")).thenReturn(Mono.just(stTest));

    webClient.mutateWith(csrf()).get().uri("/api/students/5L")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk().expectBodyList(Student.class);
  }

  @Test
  public void allStudentsIsOkTest() throws Exception {

    stTest.setStudentId("5L");
    stTest2.setStudentId("6L");
    
    when(studentService.findAll()).thenReturn(Flux.just(stTest, stTest2));

    webClient.mutateWith(csrf()).get().uri("/api/students")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
        .expectStatus().isOk().expectBody().jsonPath("$.length()").isEqualTo("7");

  }

  @Test
  public void updateStudentIsOkTest() throws Exception {

    stTest.setStudentId("5L");
    when(studentService.update(stTest.getStudentId(),stTest2)).thenReturn(Mono.just(stTest2));
    stTest2.setStudentId("5L");

    webClient.mutateWith(csrf()).put().uri("/api/students/{studentId}","5L")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(stTest2),Student.class)
            .exchange()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectStatus().isOk();
  }

  @Test
  public void deleteStudentIsOkTest() throws Exception {

    stTest.setStudentId("5L");

    when(studentService.delete(stTest.getStudentId())).thenReturn(Mono.empty());


    webClient.mutateWith(csrf()).delete()
    .uri("/api/students/{studentId}", stTest.getStudentId())
    .exchange().expectStatus().isOk();

  }

}
