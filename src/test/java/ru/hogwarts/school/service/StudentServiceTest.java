package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService out;

    @Test
    public void addStudentTest() {
        Student student = new Student(1, "Harry", 12);
        Student expected = new Student(1, "Harry", 12);
        when(studentRepository.save(student))
                .thenReturn(student);
        assertThat(out.addStudent(student))
                .isEqualTo(expected);
    }

    @Test
    public void findStudentTest() {
        Student student = new Student(2, "Ronald", 12);
        Student expected = new Student(2, "Ronald", 12);;
        when(studentRepository.findById(2L))
                .thenReturn(Optional.of(student));
        assertThat(out.findStudent(2L))
                .isEqualTo(expected);
    }

    @Test
    public void findStudentNegativeTest() {
        when(studentRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThat(out.findStudent(1L))
                .isNull();
    }

    @Test
    public void editStudentPositiveTest() {
        Student student = new Student(2, "Ron", 12);
        Student expected = new Student(2, "Ronald", 12);
        Student edit = new Student(2, "Ronald", 12);
        when(studentRepository.save(student))
                .thenReturn(edit);
        assertThat(out.editStudent(student))
                .isEqualTo(expected);
    }

    @Test
    public void deleteStudentTest() {
        out.deleteStudent(1L);
        verify(studentRepository,only()).deleteById(1L);
    }

    @Test
    public void findByAgeTest() {
        List<Student> expected = new ArrayList<>(List.of(
                new Student(1, "Harry", 12),
                new Student(3, "Draco", 12)
        ));
        List<Student> students = List.of(
                new Student(1, "Harry", 12),
                new Student(3, "Draco", 12)
        );
        when(studentRepository.findByAge(12))
                .thenReturn(students);
        assertThat(out.findByAge(12))
                .isEqualTo(expected);
    }
}
