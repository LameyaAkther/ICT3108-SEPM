
package com.example.student.service;
import java.util.List;
import org.springframework.stereotype.Service;
import com.example.student.model.Student;
import com.example.student.repository.StudentRepository;

@Service
public class StudentService {
 private final StudentRepository repo;
 public StudentService(StudentRepository repo){this.repo=repo;}
 public List<Student> getAll(){return repo.findAll();}
 public Student save(Student s){return repo.save(s);}
}
