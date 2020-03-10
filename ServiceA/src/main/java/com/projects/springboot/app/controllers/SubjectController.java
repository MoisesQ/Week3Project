package com.projects.springboot.app.controllers;

import com.projects.springboot.app.entity.Student;
import com.projects.springboot.app.entity.Subject;
import com.projects.springboot.app.entity.SubjectStudent;
import com.projects.springboot.app.service.SubjectService;
import com.projects.springboot.app.service.SubjectStudentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

  @Autowired
  SubjectService subjectService;
  
  @Autowired
  SubjectStudentService subjectStudentService;


  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Service to create a subject", consumes = MediaType.APPLICATION_JSON_VALUE, 
                produces = MediaType.APPLICATION_JSON_VALUE, 
                notes = "Save the student's information")
  @ApiResponses(value = { 
      @ApiResponse(code = 200, message = "Successfully created Subject Object"),
  })

  public Mono<Subject> postSubject(@Valid @RequestBody Subject subject) {
    return subjectService.create(subject);
  }

  @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  @ApiOperation(value = "Service to get all subjects", notes = "Get all students's information")
  @ApiResponses(value = { 
      @ApiResponse(code = 200, message = "Successfully fecthed Subject's list"),
  })
  public Flux<Subject> getSubjects() {
    return subjectService.findAll();
  }

  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Service to get a subject", notes = "Get the student's information")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully fecthed Subject Object by id"),
      @ApiResponse(code = 404, message = "The Subject Object you were trying to fetch is not found")
  })
  public Mono<Subject> getSubject(@PathVariable Long id) {
    return subjectService.findById(id);
  }

  /**
   * Update a subject object by id and then introduce new information.
   * 
   * @param id      Subject Object's id to update.
   * @param subject Subject Object to update
   * @return
   */
  @PutMapping(path = "/{id}", headers = "content-type=application/json", 
      consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Service to update a subject", notes = "Update the student's information")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully Updated Subject Object by id"),
      @ApiResponse(code = 404, message = "The Subject Object you were trying to fetch is not found")
  })
  public Mono<Subject> updateSubject(@PathVariable Long id, @Valid @RequestBody Subject subject) {
    return subjectService.update(id, subject);
  }

  /**
   * Delete a subject object by id and then introduce new information.
   * 
   * @param id Subject Object's id to delete.
   * @return
   */
  @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Service to delete a subject", notes = "Delete the student's information")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully deleted Subject Object by id"),
      @ApiResponse(code = 404, message = "The Subject Object you were trying to fetch is not found")
  })
  public Mono<Void> deleteSubject(@PathVariable Long id) {
    return subjectService.delete(id);
  }
  
  @GetMapping(path = "/{subjectId}/students", produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Service to get all students in a subject",response = Iterable.class)
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully fecthed Subject's list")
  })
  public Flux<Student> getStudentsSubjectId(@PathVariable Long subjectId) {
    return subjectStudentService.getStudentsBySubjectId(subjectId);
  }

  
  @PutMapping(path = "/{subjectId}/students/{studentId}", 
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ApiOperation(value = "Service to put a student to subject")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully inserted Subject's student")
  })
  public Mono<SubjectStudent> newStudentSubject(@PathVariable Long subjectId,
      @PathVariable String studentId) {
    return subjectStudentService.create(subjectId, studentId);
  }

  @DeleteMapping(path = "/{subjectId}/students/{studentId}")
  @ApiOperation(value = "Service to delete a student from a subject")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully deleted Subject's student")
  })
  public Mono<Void> deleteStudentSubject(@PathVariable Long subjectId, 
      @PathVariable String studentId) {
    return subjectStudentService.delete(subjectId, studentId);
  }


}
