package com.projects.springboot.app.service;

import com.projects.springboot.app.config.exception.NotFoundException;
import com.projects.springboot.app.entity.Student;
import com.projects.springboot.app.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentService {

  @Autowired
  StudentRepository studentRepository;

  public Mono<Student> create(Student student) {
    return studentRepository.save(student);
  }

  public Flux<Student> findAll() {
    return studentRepository.findAll();
  }

  public Mono<Student> findById(String id) {
    return studentRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Not found student by id " + id)));
  }

  /**
   * This method is created to update a Student Object by id.
   */
  public Mono<Student> update(String id, Student student) {
    return studentRepository.findById(id).flatMap(previousStudent -> {
      previousStudent.setGender(student.getGender());
      previousStudent.setFirstName(student.getFirstName());
      previousStudent.setMiddleName(student.getMiddleName());
      previousStudent.setLastName(student.getLastName());
      previousStudent.setDateOfBirth(student.getDateOfBirth());
      previousStudent.setOtherStudentDetails(student.getOtherStudentDetails());
      return studentRepository.save(previousStudent);
    }).switchIfEmpty(Mono.error(new NotFoundException("Not found student by id " + id)));
  }

  /**
   * This method is created to delete a Student Object by id.
   */
  public Mono<Void> delete(String id) {
    return studentRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Not found student by id " + id)))
        .flatMap(dltStudent -> studentRepository.delete(dltStudent));
  }
}