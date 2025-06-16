package com.project.project_rest_api;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.project.project_rest_api.controllers.ProjectController;
import com.project.project_rest_api.model.ProjectEntity;
import com.project.project_rest_api.services.ProjectService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
@ExtendWith(MockitoExtension.class)
public class ProjectControllerUnitTest {
    @Mock
    private ProjectService mockProjektService;
    @InjectMocks
    private ProjectController projectController;
    @Test
    void getProject_whenValidId_shouldReturnGivenProject() {
        ProjectEntity projekt = new ProjectEntity(1L, "Nazwa1", "Opis1", LocalDateTime.now());
        when(mockProjektService.findById(projekt.getId())).thenReturn(Optional.of(projekt));
        ResponseEntity<ProjectEntity> responseEntity = projectController.getProjectById(projekt.getId());
        assertAll(() -> assertEquals(responseEntity.getStatusCode().value(), HttpStatus.OK.value()),
                () -> assertEquals(responseEntity.getBody(), projekt));
    }
    @Test
    void getProject_whenInvalidId_shouldReturnNotFound() {
        Long projektId = 2L;
        when(mockProjektService.findById(projektId)).thenReturn(Optional.empty());
        ResponseEntity<ProjectEntity> responseEntity = projectController.getProjectById(projektId);
        assertEquals(responseEntity.getStatusCode().value(), HttpStatus.NOT_FOUND.value());
    }
    @Test
//@DisplayName("Should return the page containing projects")
    void getProjects_shouldReturnPageWithProjects() {
        List<ProjectEntity> list =
                List.of(new ProjectEntity(1L, "Nazwa1", "Opis1", LocalDateTime.now()),
                        new ProjectEntity(2L, "Nazwa2", "Opis2", LocalDateTime.now()),
                        new ProjectEntity(3L, "Nazwa3", "Opis3", LocalDateTime.now()));
        PageRequest pageable = PageRequest.of(1, 5);
        Page<ProjectEntity> page = new PageImpl<>(list, pageable, 5);
        when(mockProjektService.findAll(pageable)).thenReturn(page);
        Page<ProjectEntity> pageWithProjects = projectController.getProjects(pageable);
        assertNotNull(pageWithProjects);
        List<ProjectEntity> projects = pageWithProjects.getContent();
        assertNotNull(projects);
        assertThat(projects, hasSize(3));
        assertAll(() -> assertTrue(projects.contains(list.get(0))),
                () -> assertTrue(projects.contains(list.get(1))),
                () -> assertTrue(projects.contains(list.get(2))));
// W przypadku assertAll wszystkie asercje przekazane jako argumenty zostaną
// wykonane, nawet gdy jedna z pierwszych da wynik negatywny, a jeśli choć
// jedna zakończy się wyjątkiem, to cały test zakończy się błędem.
    }
    @Test
    void createProject_whenValidData_shouldCreateProject() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        ProjectEntity projekt = new ProjectEntity(1L, "Nazwa1", "Opis1", LocalDateTime.now());
        when(mockProjektService.save(any(ProjectEntity.class))).thenReturn(projekt);
        ResponseEntity<ProjectEntity> responseEntity = projectController.saveProject(projekt);
        assertThat(responseEntity.getStatusCode().value(), is(HttpStatus.CREATED.value()));
        assertThat(responseEntity.getHeaders().getLocation().getPath(), is("/" +
                projekt.getId()));
    }
    @Test
    void updateProject_whenValidData_shouldUpdateProject() {
        ProjectEntity projekt = new ProjectEntity(1L, "Nazwa1", "Opis1", LocalDateTime.now());
        when(mockProjektService.findById(projekt.getId())).thenReturn(Optional.of(projekt));
        ResponseEntity<Void> responseEntity = projectController.updateProject(projekt.getId(), projekt);
        assertThat(responseEntity.getStatusCode().value(), is(HttpStatus.OK.value()));
    }
    @Test
    void deleteProject_whenValidId_shouldDeleteProject() {
        ProjectEntity projekt = new ProjectEntity(1L, "Nazwa1", "Opis1", LocalDateTime.now());
        when(mockProjektService.findById(projekt.getId())).thenReturn(Optional.of(projekt));
        ResponseEntity<Void> responseEntity = projectController.deleteProject(projekt.getId());
        assertThat(responseEntity.getStatusCode().value(), is(HttpStatus.OK.value()));
    }
    @Test
    void deleteProject_whenInvalidId_shouldReturnNotFound() {
        Long projektId = 1L;
        ResponseEntity<Void> responseEntity = projectController.deleteProject(projektId);
        assertThat(responseEntity.getStatusCode().value(), is(HttpStatus.NOT_FOUND.value()));
    }
}