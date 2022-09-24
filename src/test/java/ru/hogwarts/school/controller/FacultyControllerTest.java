package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FacultyRepository facultyRepository;

    @SpyBean
    FacultyService facultyService;

    @InjectMocks
    FacultyController facultyController;

    @Test
    public void facultyPositiveTests() throws Exception {
        final long id = 2L;
        final String name = "Slytherin";
        final String color = "Green";
        final long idForStudent = 10L;

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        Faculty faculty = new Faculty(2L, "Slytherin", "Green");

        when(facultyRepository.save(any(Faculty.class)))
                .thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(faculty));
        when(facultyRepository.findFacultyByNameIgnoreCaseOrColorIgnoreCase(any(String.class), any(String.class)))
                .thenReturn(Collections.singletonList(faculty));
        when(facultyRepository.findById(idForStudent))
                .thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/nameOrColor?nameOrColor=" + name)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(id))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].color").value(color));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + idForStudent + "/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(facultyRepository, atLeastOnce()).findById(idForStudent);
    }

    @Test
    public void facultyNegativeTests() throws Exception {
        final long idNotFound = 3L;
        final long idForStudent = 10L;
        final long idNull = 0;
        final String nameIsBlank = "   ";

        when(facultyRepository.findById(ArgumentMatchers.eq(idNotFound)))
                .thenReturn(Optional.ofNullable(null));
        when(facultyRepository.findFacultyByNameIgnoreCaseOrColorIgnoreCase(eq(nameIsBlank), eq(nameIsBlank)))
                .thenReturn(null);
        when(facultyRepository.findById(idForStudent))
                .thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + idNotFound)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + idNotFound)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/nameOrColor?nameOrColor=" + nameIsBlank)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .equals(Collections.EMPTY_LIST);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + idForStudent + "/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + idNull + "/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}