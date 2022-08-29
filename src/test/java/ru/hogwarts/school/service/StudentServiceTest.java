package ru.hogwarts.school.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private final StudentService out = new StudentService();

    @Test
    public void addStudentTest() {
        Student expected = new Student(1, "Harry", 12);
        Student student = new Student(1, "Harry", 12);
        Assertions.assertThat(out.addStudent(student))
                .isEqualTo(expected);
    }

    @Test
    public void findStudentTest() {
        Student expected = new Student(2, "Ronald", 12);
        Student student1 = new Student(1, "Harry", 12);
        Student student2 = new Student(2, "Ronald", 12);
        out.addStudent(student1);
        out.addStudent(student2);
        Assertions.assertThat(out.findStudent(2))
                .isEqualTo(expected);
    }

    @Test
    public void editStudentPositiveTest() {
        Student student1 = new Student(1, "Harry", 12);
        Student student2 = new Student(2, "Ron", 12);
        Student expected = new Student(2, "Ronald", 12);
        Student edit = new Student(2, "Ronald", 12);
        out.addStudent(student1);
        out.addStudent(student2);
        Assertions.assertThat(out.editStudent(2, edit))
                .isEqualTo(expected);
    }

    @Test
    public void editStudentNegativeTest() {
        Student student = new Student(1, "Harry", 12);
        out.addStudent(student);
        Assertions.assertThat(out.editStudent(2, student))
                .isNull();
    }

    @Test
    public void deleteStudentTest() {
        Student student1 = new Student(1, "Harry", 12);
        Student student2 = new Student(2, "Ron", 12);
        out.addStudent(student1);
        out.addStudent(student2);
        Assertions.assertThat(out.findStudent(2))
                .isNotNull();
        out.deleteStudent(2);
        Assertions.assertThat(out.findStudent(2))
                .isNull();
    }

    @Test
    public void findByAgeTest() {
        List<Student> expected = new ArrayList<>(List.of(
                new Student(1, "Harry", 12),
                new Student(3, "Ron", 12)
        ));
        Student student1 = new Student(1, "Harry", 12);
        Student student2 = new Student(2, "Harry", 15);
        Student student3 = new Student(3, "Ron", 12);
        Student student4 = new Student(4, "Ron", 15);
        out.addStudent(student1);
        out.addStudent(student2);
        out.addStudent(student3);
        out.addStudent(student4);
        Assertions.assertThat(out.findByAge(12))
                .isEqualTo(expected);
    }
}