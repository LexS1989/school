package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FacultyServiceTest {

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyService out;

    @Test
    public void addFacultyTest() {
        Faculty faculty = new Faculty(1L, "Gryffindor", "red");
        Faculty expected = new Faculty(1L,"Gryffindor", "red");
        when(facultyRepository.save(faculty))
                .thenReturn(faculty);
        assertThat(out.addFaculty(faculty))
                .isEqualTo(expected);
    }

    @Test
    public void findFacultyTest() {
        Faculty faculty = new Faculty(2L, "Slytherin", "green");
        Faculty expected = new Faculty(2L, "Slytherin", "green");
        ;
        when(facultyRepository.findById(2L))
                .thenReturn(Optional.of(faculty));
        assertThat(out.findFaculty(2L))
                .isEqualTo(expected);
    }

    @Test
    public void findFacultyNegativeTest() {
        when(facultyRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertThat(out.findFaculty(1L))
                .isNull();
    }

    @Test
    public void editFacultyPositiveTest() {
        Faculty faculty = new Faculty(2, "Slyth", "green");
        Faculty expected = new Faculty(2, "Slytherin", "green");
        Faculty edit = new Faculty(2, "Slytherin", "green");
        when(facultyRepository.save(faculty))
                .thenReturn(edit);
        assertThat(out.editFaculty(faculty))
                .isEqualTo(expected);
    }

    @Test
    public void deleteFacultyTest() {
        out.deleteFaculty(1L);
        verify(facultyRepository, only()).deleteById(1L);
    }

    @Test
    public void findByNameOrColorTestColor() {
        List<Faculty> expected = new ArrayList<>(List.of(
                new Faculty(1, "Gryffindor", "red"),
                new Faculty(3, "Gryffindor", "red")
        ));
        List<Faculty> faculties = List.of(
                new Faculty(1, "Gryffindor", "red"),
                new Faculty(3, "Gryffindor", "red")
        );
        when(facultyRepository.findFacultyByNameIgnoreCaseOrColorIgnoreCase("red", "red"))
                .thenReturn(faculties);
        assertThat(out.findByNameOrColor("red"))
                .isEqualTo(expected);
    }
}
