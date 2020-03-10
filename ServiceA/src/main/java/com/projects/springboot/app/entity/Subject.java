package com.projects.springboot.app.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Subject {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "{Subject.name.NotBlank}")
  private String name;

  @NotBlank(message = "{Subject.description.NotBlank}")
  private String description;

  @NotNull(message = "{Subject.credits.NotNull}")
  private Float credits;

  @NotNull(message = "{Subject.hoursPerWeek.NotBlank}")
  private Float hoursPerWeek;

  public Subject() {

  }

  /**
   * Default Constructor of a Subject Object with all attributes without id.
   * 
   * @param name         Subject Object's name
   * @param description  Subject Object's's description
   * @param credits      Subject Object's's number of credits
   * @param hoursPerWeek Subject Object's's hours per week
   */
  public Subject(@NotBlank(message = "{Subject.name.NotBlank}") String name,
      @NotBlank(message = "{Subject.description.NotBlank}") String description,
      @NotNull(message = "{Subject.credits.NotNull}") Float credits,
      @NotNull(message = "{Subject.hoursPerWeek.NotBlank}") Float hoursPerWeek) {
    super();
    this.name = name;
    this.description = description;
    this.credits = credits;
    this.hoursPerWeek = hoursPerWeek;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Float getCredits() {
    return credits;
  }

  public void setCredits(Float credits) {
    this.credits = credits;
  }

  public Float getHoursPerWeek() {
    return hoursPerWeek;
  }

  public void setHoursPerWeek(Float hoursPerWeek) {
    this.hoursPerWeek = hoursPerWeek;
  }

}
