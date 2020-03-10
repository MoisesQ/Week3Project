package com.projects.springboot.app.repository;

import com.projects.springboot.app.entity.SubjectStudent;
import com.projects.springboot.app.entity.SubjectStudentEmbId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectStudentRepository extends 
                 JpaRepository<SubjectStudent, SubjectStudentEmbId> {
  
  List<SubjectStudent> findByEmbIdSubjectId(Long subjectId);


}
