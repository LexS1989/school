package ru.hogwarts.school.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;

import java.util.ArrayList;
import java.util.List;

class FacultyServiceTest {

    private final FacultyService out = new FacultyService();

    @Test
    public void addFacultyTest() {
        Faculty expected = new Faculty(1, "Gryffindor", "red");
        Faculty faculty = new Faculty(1, "Gryffindor", "red");
        Assertions.assertThat(out.addFaculty(faculty))
                .isEqualTo(expected);
    }

    @Test
    public void findFacultyTest() {
        Faculty expected = new Faculty(2, "Slytherin", "green");
        Faculty faculty1 = new Faculty(1, "Gryffindor", "red");
        Faculty faculty2 = new Faculty(2, "Slytherin", "green");
        out.addFaculty(faculty1);
        out.addFaculty(faculty2);
        Assertions.assertThat(out.findFaculty(2))
                .isEqualTo(expected);
    }

    @Test
    public void editFacultyPositiveTest() {
        Faculty faculty1 = new Faculty(1, "Gryffindor", "red");
        Faculty faculty2 = new Faculty(2, "Sly", "green");
        Faculty expected = new Faculty(2, "Slytherin", "green");
        Faculty edit = new Faculty(2, "Slytherin", "green");
        out.addFaculty(faculty1);
        out.addFaculty(faculty2);
        Assertions.assertThat(out.editFaculty(2, edit))
                .isEqualTo(expected);

    }

    @Test
    public void editFacultyNegativeTest() {
        Faculty faculty = new Faculty(1, "Gryffindor", "red");
        out.addFaculty(faculty);
        Assertions.assertThat(out.editFaculty(2, faculty))
                .isNull();
    }

    @Test
    public void deleteFacultyTest() {
        Faculty faculty1 = new Faculty(1, "Gryffindor", "red");
        Faculty faculty2 = new Faculty(2, "Slytherin", "green");
        out.addFaculty(faculty1);
        out.addFaculty(faculty2);
        Assertions.assertThat(out.findFaculty(2))
                .isNotNull();
        out.deleteFaculty(2);
        Assertions.assertThat(out.findFaculty(2))
                .isNull();
    }

    @Test
    public void findByColorTest() {
        List<Faculty> expected = new ArrayList<>(List.of(
                new Faculty(1, "Gryffindor", "red"),
                new Faculty(3, "Gryffindor", "red")
        ));
        Faculty faculty1 = new Faculty(1, "Gryffindor", "red");
        Faculty faculty2 = new Faculty(2, "Slytherin", "green");
        Faculty faculty3 = new Faculty(3, "Gryffindor", "red");
        Faculty faculty4 = new Faculty(4, "Slytherin", "green");
        out.addFaculty(faculty1);
        out.addFaculty(faculty2);
        out.addFaculty(faculty3);
        out.addFaculty(faculty4);
        Assertions.assertThat(out.findByColor("red"))
                .isEqualTo(expected);
    }
}