package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoadTest() throws Exception {
        assertThat(studentController).isNotNull();
    }

    @Test
    public void createStudentTest() throws Exception {
        Student student = new Student(1L, "Harry", 12);
        Student expected = new Student(1L, "Harry", 12);
        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student/", student, Student.class))
                .isEqualTo(expected);
    }

    @Test
    public void getStudentInfoPositiveTest() throws Exception {
        Student student = new Student(1L, "Harry", 12);
        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student, Student.class);
        Student expected = new Student(1L, "Harry", 12);
        long id = 1L;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id, Student.class))
                .isEqualTo(expected);
    }

    @Test
    public void getStudentInfoNegativeTest() throws Exception {
        long id = 3L;
        assertThat(this.restTemplate.getForEntity("http://localhost:" + port + "/student/" + id, Student.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void editStudentTest() throws Exception {
        Student student = new Student(1L, "Harry", 12);
        HttpEntity<Student> studentEntity = new HttpEntity<>(student);
        Student expected = new Student(1L, "Harry", 12);

        this.restTemplate.exchange("http://localhost:" + port + "/student/",
                HttpMethod.POST,
                studentEntity,
                Student.class);

        long id = 1L;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id, Student.class))
                .isEqualTo(expected);

        Student studentEdit = new Student(1L, "Ron", 12);
        HttpEntity<Student> studentEntityEdit = new HttpEntity<>(studentEdit);
        Student expectedEdit = new Student(1L, "Ron", 12);

        restTemplate.exchange("http://localhost:" + port + "/student/",
                HttpMethod.PUT,
                studentEntityEdit,
                Student.class);

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id, Student.class))
                .isEqualTo(expectedEdit);
    }

    @Test
    public void deleteStudent() throws Exception {
        Student student = new Student(1L, "Harry", 12);
        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student, Student.class);
        Student expected = new Student(1L, "Harry", 12);
        long id = 1L;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id, Student.class))
                .isEqualTo(expected);
        this.restTemplate.delete("http://localhost:" + port + "/student/" + id);
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id, Student.class))
                .isNull();
    }

    @Test
    public void findByAgePositiveTest() throws Exception {
        Student student1 = new Student(1L, "Harry", 12);
        Student student2 = new Student(2L, "Ronald", 13);
        Student student3 = new Student(3L, "Hermione", 13);
        Student student4 = new Student(4L, "Draco", 15);

        Collection<Student> expected = List.of(student2, student3);

        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student1, Student.class);
        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student2, Student.class);
        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student3, Student.class);
        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student4, Student.class);

        MultiValueMap<String, String> age = new LinkedMultiValueMap<>();
        age.add("age", "13");
        URI uri = getUriBuilder().queryParams(age).build().toUri();

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

        Collection<Student> actualResult = response.getBody();

        assertThat(actualResult)
                .isEqualTo(expected);
    }

    @Test
    public void findByAgeNegativeTest() throws Exception {
        Collection<Student> expected = Collections.emptyList();

        MultiValueMap<String, String> age = new LinkedMultiValueMap<>();
        age.add("age", "0");
        URI uri = getUriBuilder().queryParams(age).build().toUri();

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

        Collection<Student> actualResult = response.getBody();

        assertThat(actualResult)
                .isEqualTo(expected);
    }

    @Test
    public void findByAgeBetweenPositiveTest() throws Exception {
        Student student1 = new Student(1L, "Harry", 12);
        Student student2 = new Student(2L, "Ronald", 14);
        Student student3 = new Student(3L, "Hermione", 15);
        Student student4 = new Student(4L, "Draco", 17);

        Collection<Student> expected = List.of(student2, student3);

        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student1, Student.class);
        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student2, Student.class);
        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student3, Student.class);
        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student4, Student.class);

        int minAge = 13;
        int maxAge = 16;

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/ageBetween?minAge=" + minAge + "&maxAge=" + maxAge,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

        Collection<Student> actualResult = response.getBody();

        assertThat(actualResult)
                .isEqualTo(expected);
    }

    @Test
    public void findByAgeBetweenNegativeNullTest() throws Exception {
        int minAge = -1;
        int maxAge = 12;

        Collection<Student> expected = Collections.emptyList();

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/ageBetween?minAge=" + minAge + "&maxAge=" + maxAge,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

        Collection<Student> actualResult = response.getBody();

        assertThat(actualResult)
                .isEqualTo(expected);
    }

    @Test
    public void findByAgeBetweenNegativeBadRequestTest() throws Exception {
        int minAge = 13;
        int maxAge = 12;

        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                "http://localhost:" + port + "/student/ageBetween?minAge=" + minAge + "&maxAge=" + maxAge,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {
                }
        );

        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void findFacultyByStudentTest() throws Exception {
        Student student = new Student(1L, "Harry", 12);
//        Faculty faculty = new Faculty(2L, "Slytherin", "Green");
//        student.setFaculty(faculty);
//        Faculty expected = new Faculty(2L, "Slytherin", "Green");
        this.restTemplate.postForObject("http://localhost:" + port + "/student/", student, Student.class);
        long id = 1L;
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student/" + id + "/faculty", Faculty.class))
                .isNull();
    }

    private UriComponentsBuilder getUriBuilder() {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path("/student/");
    }
}