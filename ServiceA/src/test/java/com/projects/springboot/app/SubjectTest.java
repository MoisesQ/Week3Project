package com.projects.springboot.app;

import static org.mockito.Mockito.when;

//import com.projects.springboot.app.config.SchedulerConfig;
import com.projects.springboot.app.controllers.SubjectController;
import com.projects.springboot.app.entity.Subject;
import com.projects.springboot.app.entity.SubjectStudent;
import com.projects.springboot.app.entity.SubjectStudentEmbId;
import com.projects.springboot.app.service.EndpointFeignService;
import com.projects.springboot.app.service.SubjectService;
import com.projects.springboot.app.service.SubjectStudentService;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(SubjectController.class)
//@ContextConfiguration(classes = {SchedulerConfig.class})
public class SubjectTest {

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private SubjectService subjectService;
  
  @MockBean
  private SubjectStudentService subjectStudentService;
  
  @MockBean
  private EndpointFeignService endpointFeignService;

  private Subject subTest;
  private Subject subTest2;
  /**
   * This method is created because we need to start with a student Object to use
   * in all tests.
   * 
   * @throws ParseException Using this exception because the date string format
   *                        doesn't work in the default constructor
   */
  @BeforeEach
  public void beforeAll() throws ParseException {
    subTest = new Subject("Estadística Inferencial", 
        "Estadística que prioriza la inferenciación", 6f, 4f);
    subTest2 = new Subject("Estadística Descriptiva", 
        "Estadística que prioriza la descripción", 6f, 3.5f);

  }

  @Test

  public void createSubjectIsOkTest() {

    when(subjectService.create(subTest)).thenReturn(Mono.just(subTest));

    webClient.post().uri("/api/subjects").contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(subTest), Subject.class)
        .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON).expectStatus().isOk();

  }

  @Test
  public void findOneSubjectIsOkTest() throws Exception {

    when(subjectService.findById(15L)).thenReturn(Mono.just(subTest));

    webClient.get().uri("/api/subjects/{id}", 15L)
        .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk().expectBodyList(Subject.class);
  }

  @Test
  public void allSubjectsIsOkTest() throws Exception {

    subTest.setId(15L);
    subTest2.setId(16L);

    when(subjectService.findAll()).thenReturn(Flux.just(subTest, subTest2));

    webClient.get().uri("/api/subjects").exchange()
        .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
        .expectStatus().isOk().expectBody()
        .jsonPath("$.length()").isEqualTo("5");

  }

  @Test
  public void updateSubjectIsOkTest() throws Exception {

    subTest.setId(15L);
    when(subjectService.findById(subTest.getId())).thenReturn(Mono.just(subTest));
    when(subjectService.update(subTest.getId(), subTest2)).thenReturn(Mono.just(subTest2));
    subTest2.setId(15L);

    webClient.put().uri("/api/subjects/{id}", 15L)
        .contentType(MediaType.APPLICATION_JSON).body(Mono.just(subTest), Subject.class)
        .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk();
  }

  @Test
  public void deleteSubjectIsOkTest() throws Exception {

    subTest.setId(15L);

    when(subjectService.findById(subTest.getId())).thenReturn(Mono.just(subTest));
    when(subjectService.delete(subTest.getId())).thenReturn(Mono.empty());

    webClient.delete().uri("/api/subjects/{id}", subTest.getId())
        .exchange().expectStatus().isOk();

  }
  
  @Test
  void getStudentsOfSubjectIsOk() {
    subTest.setId(15L);


    webClient.get().uri("/api/subjects/" + 15L + "/students")
            .exchange()
            .expectStatus().isOk();
  }
  
  
  
  @Test
  void addStudentToSubjectIsOk() {
    subTest.setId(15L);

    SubjectStudent subjectStudent = new SubjectStudent();
    subjectStudent.setEmbId(new SubjectStudentEmbId());
    subjectStudent.setCreationDate(new Date());

    when(subjectService.findById(15L)).thenReturn(Mono.just(subTest));

    webClient.put().uri("/api/subjects/" + 15L+ "/students/123")
            .exchange()
            .expectStatus().isOk();
  }

  @Test
  void deleteStudentSubjectIsOk() {
    subTest.setId(15L);

    SubjectStudent subjectStudent = new SubjectStudent();
    subjectStudent.setEmbId(new SubjectStudentEmbId());
    subjectStudent.setCreationDate(new Date());

//    when(subjectStudentService.create(subjectStudent.getEmbId().setSubjectId(subTest.getId()), subjectStudent.getEmbId().setStudentId("123")))
//    .thenReturn(subjectStudent);
    when(subjectService.findById(15L)).thenReturn(Mono.just(subTest));

    webClient.delete().uri("/api/subjects/" + 15L + "/students/123")
            .exchange()
            .expectStatus().isOk();
  }
  

}
