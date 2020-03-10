package com.projects.springboot.app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

@Entity
public class SubjectStudent {

  @EmbeddedId
  @JsonIgnore
  private SubjectStudentEmbId embId;
  
  @NotNull(message = "{SubjectStudent.creationDate.NotNull}")
  @Column(name = "creation_date")
  private Date creationDate;
  
  public SubjectStudent() {

  }

  /**
   * SubjectStudent default constructor method.
   * @param embId SubjectStudentEmbId embedded id.
   * @param creationDate SubjectStudentEmbId creation date.
   */
  public SubjectStudent(SubjectStudentEmbId embId,
      @NotNull(message = "{SubjectStudent.creationDate.NotNull}") Date creationDate) {
    this.embId = embId;
    this.creationDate = creationDate;
  }

  public SubjectStudentEmbId getEmbId() {
    return embId;
  }

  public void setEmbId(SubjectStudentEmbId embId) {
    this.embId = embId;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

}
