package com.projects.springboot.app.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class SubjectStudentEmbId implements Serializable {

  @Column(name = "subject_id")
  private Long subjectId;

  @Column(name = "student_id")
  private String studentId;

  public Long getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(Long subjectId) {
    this.subjectId = subjectId;
  }

  public String getStudentId() {
    return studentId;
  }

  public void setStudentId(String studentId) {
    this.studentId = studentId;
  }

  public SubjectStudentEmbId() {

  }

  public SubjectStudentEmbId(Long subjectId, String studentId) {
    this.subjectId = subjectId;
    this.studentId = studentId;
  }

  private static final long serialVersionUID = 1L;

}
