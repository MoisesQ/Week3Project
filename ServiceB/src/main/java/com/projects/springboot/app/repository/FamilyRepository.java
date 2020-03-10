package com.projects.springboot.app.repository;

import com.projects.springboot.app.entity.Family;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FamilyRepository extends ReactiveMongoRepository<Family, String> {
  
}
