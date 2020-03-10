package com.projects.springboot.app.service;

import com.projects.springboot.app.config.exception.NotFoundException;
import com.projects.springboot.app.entity.Parent;
import com.projects.springboot.app.repository.ParentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ParentService {

  @Autowired
  ParentRepository parentRepository;

  public Mono<Parent> create(Parent parent) {
    return parentRepository.save(parent);
  }

  public Flux<Parent> findAll() {
    return parentRepository.findAll();
  }

  public Mono<Parent> findById(String id) {
    return parentRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Not found parent by id " + id)));
  }

  /**
   * This method is created to update a Parent Object by id.
   */
  public Mono<Parent> update(String id, Parent parent) {
    return parentRepository.findById(id).flatMap(previousParent -> {
      previousParent.setGender(parent.getGender());
      previousParent.setFirstName(parent.getFirstName());
      previousParent.setMiddleName(parent.getMiddleName());
      previousParent.setLastName(parent.getLastName());
      previousParent.setOtherParentDetails(parent.getOtherParentDetails());
      return parentRepository.save(previousParent);
    }).switchIfEmpty(Mono.error(new NotFoundException("Not found parent by id " + id)));
  }

  /**
   * This method is created to delete a Parent Object by id.
   */
  public Mono<Void> delete(String id) {
    return parentRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Not found parent by id " + id)))
        .flatMap(dltParent -> parentRepository.delete(dltParent));
  }
}
