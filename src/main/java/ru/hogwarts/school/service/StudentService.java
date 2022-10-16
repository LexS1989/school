package ru.hogwarts.school.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;
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

    public void threadStudents() {
        List<String> allStudents = new ArrayList<>();

        studentRepository.findAll().stream()
                .map(e -> e.getName())
                .map(e -> allStudents.add(e))
                .collect(Collectors.toList());

        printNameInConsole(allStudents.get(0));
        printNameInConsole(allStudents.get(1));

        new Thread(() -> {
            printNameInConsole(allStudents.get(2));
            printNameInConsole(allStudents.get(3));
        }).start();

        new Thread(() -> {
            printNameInConsole(allStudents.get(4));
            printNameInConsole(allStudents.get(5));
        }).start();
    }

    public void synchronizeThreadStudent(){
        List<String> allStudents = new ArrayList<>();

        studentRepository.findAll().stream()
                .map(e -> e.getName())
                .map(e -> allStudents.add(e))
                .collect(Collectors.toList());

        printNameInConsoleSynchronize(allStudents.get(0));
        printNameInConsoleSynchronize(allStudents.get(1));

        new Thread(() -> {
            printNameInConsoleSynchronize(allStudents.get(2));
            printNameInConsoleSynchronize(allStudents.get(3));
        }).start();

        new Thread(() -> {
            printNameInConsoleSynchronize(allStudents.get(4));
            printNameInConsoleSynchronize(allStudents.get(5));
        }).start();
    }

    public void printNameInConsole(String name) {
        System.out.println(name);
    }

    public synchronized void printNameInConsoleSynchronize(String name) {
        System.out.println(name);
    }
}
