package ru.hogwarts.school.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student) {
        logger.debug("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.debug("Was invoked method for get student info with id = {} ", id);
        Student student = studentRepository.findById(id).orElse(null);
        logger.warn("Response {} ", student);
        return student;
    }

    public Student editStudent(Student student) {
        logger.debug("Was invoked method for edith students");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.debug("Was invoked method for delete student");
        studentRepository.deleteById(id);
    }

    public Collection<Student> findByAge(int age) {
        logger.debug("Was invoked method for find by age");
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max) {
        logger.debug("Was invoked method for find by age between");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Integer getCountAllStudent() {
        logger.debug("Was invoked method for find get count student");
        return studentRepository.countAllStudentById();
    }

    public Double findAverageAge() {
        logger.debug("Was invoked method for find get average age");
        return studentRepository.findAverageAge();
    }

    public Collection<Student> fiveLastStudents() {
        logger.debug("Was invoked method for find get last five students");
        return studentRepository.findLastFiveStudentsById();
    }

    public Collection<String> findNameByFirstLetter() {
        return studentRepository.findAll().stream()
                .map(e -> e.getName())
                .map(e -> StringUtils.lowerCase(e))
                .map(e -> StringUtils.capitalize(e))
                .filter(e -> e.startsWith("А"))
                .sorted()
                .collect(Collectors.toList());
    }

    public double averageAge() {
        return studentRepository.findAll().stream()
                .mapToDouble(e -> e.getAge())
                .average().orElse(0);
    }
}
