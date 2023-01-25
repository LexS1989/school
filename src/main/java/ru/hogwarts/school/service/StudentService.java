package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@Service
public class StudentService {

    private final HashMap<Long, Student> students = new HashMap<>();
    private long count = 0;

    public Student addStudent(Student student) {
        student.setId(++count);
        students.put(student.getId(), student);
        return student;
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(long id, Student student) {
        if (students.containsKey(id)) {
            students.put(id, student);
            return student;
        }
        return null;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }

    public Collection<Student> findByAge(int age) {
        ArrayList<Student> ageStudents = new ArrayList<>();
        for (Student student : students.values()) {
            if (Objects.equals(student.getAge(), age)) {
                ageStudents.add(student);
            }
        }
        return ageStudents;
    }
}
