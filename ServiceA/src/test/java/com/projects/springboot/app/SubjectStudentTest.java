package com.projects.springboot.app;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
//import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.projects.springboot.app.entity.Student;
import com.projects.springboot.app.entity.Subject;
import com.projects.springboot.app.entity.SubjectStudent;
import com.projects.springboot.app.entity.SubjectStudentEmbId;
import com.projects.springboot.app.repository.SubjectRepository;
import com.projects.springboot.app.repository.SubjectStudentRepository;
import com.projects.springboot.app.service.EndpointFeignService;
import com.projects.springboot.app.service.SubjectService;
import com.projects.springboot.app.service.SubjectStudentService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@WebFluxTest(SubjectStudentService.class)
public class SubjectStudentTest {

  @MockBean
  SubjectStudentService subjectStudentService;

  @MockBean
  SubjectRepository subjectRepository;

  @MockBean
  SubjectStudentRepository subjectStudentRepository;

  @Autowired
  WebTestClient webTestClient;

  @MockBean
  EndpointFeignService studentFeignService;

  @MockBean
  SubjectService subjectService;

  private SubjectStudentEmbId subjectStudentEmbId;

  private SubjectStudent subjectStudent;

  private Student student;

  private Subject subject;

  @BeforeEach
  public void beforeAll() throws ParseException {
    student = new Student();
    student.setDateOfBirth(new Date());
    student.setFirstName("Eric");
    student.setGender("Male");
    student.setStudentId("123");
    student.setLastName("Cartman");
    student.setMiddleName("F.");
    student.setOtherStudentDetails("Other details");

    subject = new Subject();
    subject.setCredits(2.0f);
    subject.setId(1L);
    subject.setName("HIS101");
    subject.setDescription("History class");
    subject.setHoursPerWeek(3.0f);

    subjectStudentEmbId = new SubjectStudentEmbId();
    subjectStudentEmbId.setStudentId(student.getStudentId());
    subjectStudentEmbId.setSubjectId(subject.getId());

    subjectStudent = new SubjectStudent();
    subjectStudent.setCreationDate(new Date());
    subjectStudent.setEmbId(subjectStudentEmbId);
    subjectStudent.getEmbId().setStudentId(student.getStudentId());
    subjectStudent.getEmbId().setSubjectId(subject.getId());
  }

  @Test
  void findByIdIsOk() {
    when(subjectStudentRepository.findById(subjectStudent.getEmbId()))
            .thenReturn(Optional.of(subjectStudent));

  }

  @Test
  void findByIdIsNotOk() {
    when(subjectStudentRepository.findById(subjectStudent.getEmbId()))
            .thenReturn(Optional.empty());

    StepVerifier.create(subjectStudentService.findById(subjectStudent.getEmbId()));
//            .verifyError();
  }

  @Test
  void findAllIsOk() {
    SubjectStudentEmbId subjectStudentId1 = new SubjectStudentEmbId();
    subjectStudentId1.setStudentId("123");
    subjectStudentId1.setSubjectId(1L);

    SubjectStudent subjectStudent1 = new SubjectStudent();
    subjectStudent1.setCreationDate(new Date());
    subjectStudent1.setEmbId(subjectStudentId1);
    
    List<SubjectStudent> subjectStudents = Arrays.asList(subjectStudent, subjectStudent1);

    when(subjectStudentRepository.findAll())
            .thenReturn(subjectStudents);

    StepVerifier.create(subjectStudentService.findAll())
            .expectNext(subjectStudent)
            .expectNext(subjectStudent1);
//            .verifyComplete();
  }

  @Test
  void createIsOk() {
    when(subjectService.findById(any())).thenReturn(Mono.just(subject));
    when(subjectStudentRepository.save(subjectStudent)).thenReturn(subjectStudent);
  }

  @Test
  void createIsNotOk() {
    Subject subject = new Subject();
    subject.setCredits(2.0f);
    subject.setId(1L);
    subject.setName("HIS101");
    subject.setDescription("History class");
    subject.setHoursPerWeek(3.0f);

    when(subjectService.findById(any())).thenReturn(Mono.empty());
    when(subjectStudentRepository.save(subjectStudent)).thenReturn(subjectStudent);

    StepVerifier.create(subjectStudentService.create(subject.getId(),"123"));
//            .verifyError();
  }

  
}
