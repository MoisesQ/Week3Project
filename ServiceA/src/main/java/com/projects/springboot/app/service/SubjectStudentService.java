package com.projects.springboot.app.service;

import com.projects.springboot.app.config.exception.NotFoundException;
import com.projects.springboot.app.entity.Student;
import com.projects.springboot.app.entity.SubjectStudent;
import com.projects.springboot.app.entity.SubjectStudentEmbId;
import com.projects.springboot.app.repository.SubjectStudentRepository;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Service
public class SubjectStudentService {

  @Autowired
  @Qualifier("jdbcScheduler")
  Scheduler jdbcScheduler;

  @Autowired
  SubjectStudentRepository subjectStudentRepository;
  
  @Autowired
  SubjectService subjectService;
  
  @Autowired
  EndpointFeignService endpointFeignService;

  // AsyncCall Methods
  protected <S> Mono<S> asyncCall(Callable<S> callable) {
    return Mono.fromCallable(callable).subscribeOn(Schedulers.parallel()).publishOn(jdbcScheduler);
  }

  protected <S> Flux<S> asyncIt(Iterable<S> iterable) {
    return Flux.fromIterable(iterable).subscribeOn(Schedulers.parallel()).publishOn(jdbcScheduler);
  }

  // Service Methods
  /**
   * Create a SubjectStudent Object.
   * @param subjectId Subject id.
   * @param studentId Student id.
   * @return
   */
  public Mono<SubjectStudent> create(Long subjectId, String studentId) {
    SubjectStudent subjectStudent = new SubjectStudent(
        new SubjectStudentEmbId(subjectId, studentId), new Date());
    return subjectService.findById(subjectStudent.getEmbId().getSubjectId())
            .flatMap(subject -> {
              return asyncCall(() -> subjectStudentRepository.save(subjectStudent));
            })
            .switchIfEmpty(Mono.error(
                    new NotFoundException("Not found subject by Id: " 
            + subjectStudent.getEmbId().getSubjectId())));
  }


  public Flux<SubjectStudent> findAll() {
    return asyncIt(() -> subjectStudentRepository.findAll().iterator());
  }
  
  /**
   * Find a SubjectStudent Object by id.
   * @param embId SubjectStudent object's id to find.
   * @return a founded Mono SubjectStudent.
   */
  public Mono<SubjectStudent> findById(SubjectStudentEmbId embId) {
    return asyncCall(() -> subjectStudentRepository.findById(embId))
        .flatMap(optionalSubjectStudent -> {
          optionalSubjectStudent
              .orElseThrow(() -> new NotFoundException("Not found subjectStudent by Id: " 
              + embId));
          return Mono.just(optionalSubjectStudent.get());
        })
        .switchIfEmpty(Mono.error(new NotFoundException("Not found subjectStudent by Id: " 
          + embId)));
  }


  /**
   * Delete a SubjectStudent Object.
   * @param subjectId Subject id.
   * @param studentId Student id.
   * @return
   */
  public Mono<Void> delete(Long subjectId, String studentId) {
    SubjectStudentEmbId embId = new SubjectStudentEmbId(subjectId, studentId);
    return this.findById(embId)
            .flatMap(fndsubjectStudent ->
              asyncCall(() -> {
                subjectStudentRepository.delete(fndsubjectStudent);
                return null;
              })
            );
  }
  
  /**
   * Returns Subject students's Flux.
   * @param subjectId Subject's id.
   * @return Subject students's Flux.
   */
  public Flux<Student> getStudentsBySubjectId(Long subjectId) {
    List<SubjectStudent> subjectStudents = subjectStudentRepository
            .findByEmbIdSubjectId(subjectId);
    List<String> studentIds = subjectStudents.stream()
            .map(s -> s.getEmbId().getStudentId())
            .collect(Collectors.toList());
    return endpointFeignService.getSpecificStudents(studentIds);
  }




}