package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

@Service
public class FacultyService {

    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private long count = 0;

    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++count);
        faculties.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty findFaculty(long id) {
        return faculties.get(id);
    }

    public Faculty editFaculty(long id, Faculty faculty) {
        if (faculties.containsKey(id)) {
            faculties.put(id, faculty);
            return faculty;
        }
        return null;
    }

    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }

    public Collection<Faculty> findByColor(String color) {
        ArrayList<Faculty> nameFaculty = new ArrayList<>();
        for (Faculty faculty : faculties.values()) {
            if (Objects.equals(faculty.getColor(), color)) {
                nameFaculty.add(faculty);
            }
        }
        return nameFaculty;
    }
}
