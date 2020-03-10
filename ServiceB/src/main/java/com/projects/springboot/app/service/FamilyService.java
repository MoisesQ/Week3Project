package com.projects.springboot.app.service;

import com.projects.springboot.app.config.exception.NotFoundException;
import com.projects.springboot.app.entity.Family;
import com.projects.springboot.app.repository.FamilyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class FamilyService {

  @Autowired
  FamilyRepository familyRepository;

  public Mono<Family> create(Family family) {
    return familyRepository.save(family);
  }

  public Flux<Family> findAll() {
    return familyRepository.findAll();
  }

  public Mono<Family> findById(String id) {
    return familyRepository.findById(id);
  }

  /**
   * This method is created to update a Family Object by id.
   */
  public Mono<Family> update(String id, Family family) {
    return familyRepository.findById(id).flatMap(previousFamily -> {
      previousFamily.setFamilyName(family.getFamilyName());
      previousFamily.setHeadOfFamilyParentId(family.getHeadOfFamilyParentId());
      return familyRepository.save(previousFamily);
    }).switchIfEmpty(Mono.error(new NotFoundException("Not found family by id " + id)));
  }

  /**
   * This method is created to delete a Family Object by id.
   */
  public Mono<Void> delete(String id) {
    return familyRepository.findById(id)
        .switchIfEmpty(Mono.error(new NotFoundException("Not found student by id " + id)))
        .flatMap(dltFamily -> familyRepository.delete(dltFamily));
  }

}
