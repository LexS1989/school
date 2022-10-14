package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentInfo(@PathVariable long id) {
        Student foundStudent = studentService.findStudent(id);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @PutMapping
    public Student editStudent(@RequestBody Student student) {
        return studentService.editStudent(student);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable long id) {
        Student foundStudent = studentService.findStudent(id);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findByAge(@RequestParam int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.findByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/ageBetween")
    public ResponseEntity<Collection<Student>> findByAgeBetween(@RequestParam int minAge,
                                                                @RequestParam int maxAge) {
        if (maxAge < minAge) {
            return ResponseEntity.badRequest().build();
        }
        if (minAge >= 0) {
            return ResponseEntity.ok(studentService.findByAgeBetween(minAge, maxAge));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> findFacultyByStudent(@PathVariable long id) {
        Student foundStudent = studentService.findStudent(id);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent.getFaculty());
    }

    @GetMapping("/count-student")
    public Integer getCountStudent() {
        return studentService.getCountAllStudent();
    }

    @GetMapping("/average-age")
    public Double getAverageAge() {
        return studentService.findAverageAge();
    }

    @GetMapping("/last-students")
    public ResponseEntity<Collection<Student>> getLastFiveStudents() {
        Collection<Student> lastStudents = studentService.fiveLastStudents();
        return ResponseEntity.ok(lastStudents);
    }

    @GetMapping("/sorted-list")
    public ResponseEntity<Collection<String>> sortedNameByFirstLetter() {
        return ResponseEntity.ok(studentService.findNameByFirstLetter());
    }

    @GetMapping("/average-age-student")
    public ResponseEntity<Double> averageAge() {
        double age = studentService.averageAge();
        if (age == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(age);
    }
}
