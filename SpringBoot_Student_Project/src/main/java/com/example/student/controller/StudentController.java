
package com.example.student.controller;
import java.util.List;
import org.springframework.web.bind.annotation.*;
import com.example.student.model.Student;
import com.example.student.service.StudentService;

@RestController
@RequestMapping("/students")
public class StudentController {
 private final StudentService service;
 public StudentController(StudentService service){this.service=service;}
 @GetMapping
 public List<Student> allStudents(){return service.getAll();}
 @PostMapping
 public Student addStudent(@RequestBody Student s){return service.save(s);}
}
