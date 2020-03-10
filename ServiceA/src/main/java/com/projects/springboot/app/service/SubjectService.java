package com.projects.springboot.app.service;

import com.projects.springboot.app.config.exception.NotFoundException;
import com.projects.springboot.app.entity.Subject;
import com.projects.springboot.app.repository.SubjectRepository;
import java.util.concurrent.Callable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Service
public class SubjectService {

  @Autowired
  @Qualifier("jdbcScheduler")
  Scheduler jdbcScheduler;

  @Autowired
  SubjectRepository subjectRepository;

  // AsyCall Methods
  protected <S> Mono<S> asyncCall(Callable<S> callable) {
    return Mono.fromCallable(callable).subscribeOn(Schedulers.parallel()).publishOn(jdbcScheduler);
  }

  protected <S> Flux<S> asyncIt(Iterable<S> iterable) {
    return Flux.fromIterable(iterable).subscribeOn(Schedulers.parallel()).publishOn(jdbcScheduler);
  }

  // Service Methods
  public Mono<Subject> create(Subject subject) {
    return asyncCall(() -> subjectRepository.save(subject));
  }

  public Flux<Subject> findAll() {
    return asyncIt(() -> subjectRepository.findAll().iterator());
  }

  /**
   * Find a Subject Object by id.
   * 
   * @param id Subject object's id to find.
   * @return a founded Mono Subject .
   */

  public Mono<Subject> findById(Long id) {
    return asyncCall(() -> subjectRepository.findById(id)).flatMap(optionalSubject -> {
      optionalSubject.orElseThrow(() -> new NotFoundException("Not found subject by Id: " + id));
      return Mono.just(optionalSubject.get());
    }).switchIfEmpty(Mono.error(new NotFoundException("Not found subject by Id: " + id)));
  }

  /**
   * Update a Subject Object.
   * 
   * @param id Subject object's id to update
   * @param subject subject Subject Object
   * @return an updated Mono Subject.
   */

  public Mono<Subject> update(Long id, Subject subject) {
    return this.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Not found subject by Id: " + id)))
        .flatMap(foundedSubject -> {
          foundedSubject.setName(subject.getName());
          foundedSubject.setDescription(subject.getDescription());
          foundedSubject.setHoursPerWeek(subject.getHoursPerWeek());
          foundedSubject.setCredits(subject.getCredits());
          return asyncCall(() -> subjectRepository.save(foundedSubject));
        });
  }

  /**
   * Delete a Subject Object.
   * @param id Subject object's id to delete
   * @return an deleted Mono Void.
   */
  public Mono<Void> delete(Long id) {
    return this.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Not found subject by Id: " + id)))
        .flatMap(foundedSubject -> asyncCall(() -> {
          subjectRepository.delete(foundedSubject);
          return null;
        }));
  }
}