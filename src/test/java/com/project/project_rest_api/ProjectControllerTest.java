package com.project.project_rest_api;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.project.project_rest_api.model.ProjectEntity;
import com.project.project_rest_api.services.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "admin", password = "admin")
public class ProjectControllerTest {
    // Uwaga! Test wymaga poniższego konstruktora w klasie Projekt, dodaj jeżeli nie został jeszcze zdefiniowany.
// public Projekt(Integer projektId, String nazwa, String opis, LocalDateTime dataCzasUtworzenia, LocalDate
//dataOddania){
// ...
//}
// --- URUCHAMIANIE TESTÓW ---
// ABY URUCHOMIĆ TESTY KLIKNIJ NA NAZWIE KLASY PRAWYM PRZYCISKIEM
// MYSZY I WYBIERZ Z MENU 'Run As' -> 'Gradle Test' LUB PO USTAWIENIU
// KURSORA NA NAZWIE KLASY WCIŚNIJ SKRÓT 'CTRL+ALT+X' A PÓŹNIEJ 'G'
// MOŻNA RÓWNIEŻ ANALOGICZNIE URUCHAMIAĆ POJEDYNCZE METODY KLIKAJĄC
// WCZEŚNIEJ NA ICH NAZWĘ
    private final String apiPath = "/api/v1/projects";
    @MockitoBean
    private ProjectService mockProjektService; // tzw. mock (czyli obiekt, którego używa się zamiast rzeczywistej
    // implementacji) serwisu wykorzystywany przy testowaniu kontrolera
    @Autowired
    private MockMvc mockMvc;
    private JacksonTester<ProjectEntity> jacksonTester;
    @Test
    public void getProject_whenValidId_shouldReturnGivenProject() throws Exception {
        ProjectEntity projekt = new ProjectEntity(2L, "Nazwa2", "Opis2", LocalDateTime.now(), LocalDateTime.now());
        when(mockProjektService.findById(projekt.getId()))
                .thenReturn(Optional.of(projekt));
        mockMvc.perform(get(apiPath + "/{id}", projekt.getId()).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projektId").value(projekt.getId()))
                .andExpect(jsonPath("$.nazwa").value(projekt.getName()));
        verify(mockProjektService, times(1)).findById(projekt.getId());
        verifyNoMoreInteractions(mockProjektService);
    }
    @Test
    public void getProject_whenInvalidId_shouldReturnNotFound() throws Exception {
        Long projektId = 2L;
        when(mockProjektService.findById(projektId)).thenReturn(Optional.empty());
        mockMvc.perform(get(apiPath + "/{id}", projektId).accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(mockProjektService, times(1)).findById(projektId);
        verifyNoMoreInteractions(mockProjektService);
    }
    @Test
    public void getProjects_whenTwoAvailable_shouldReturnContentWithPagingParams() throws Exception {
        ProjectEntity projekt1 = new ProjectEntity(1L, "Nazwa1", "Opis1", LocalDateTime.now(), LocalDateTime.now());
        ProjectEntity projekt2 = new ProjectEntity(2L, "Nazwa2", "Opis2", LocalDateTime.now(), LocalDateTime.now());
        ProjectEntity projekt3 = new ProjectEntity(1L, "Nazwa1", "Opis1", LocalDateTime.now(), LocalDateTime.now());
        ProjectEntity projekt4 = new ProjectEntity(2L, "Nazwa2", "Opis2", LocalDateTime.now(), LocalDateTime.now());
        Page<ProjectEntity> page = new PageImpl<>(List.of(projekt1, projekt2));
        when(mockProjektService.findAll(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get(apiPath).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[*]").exists()) //content[*] - oznacza całą zawartość tablicy content
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].projektId").value(projekt1.getId()))
                .andExpect(jsonPath("$.content[1].projektId").value(projekt2.getId()));
        verify(mockProjektService, times(1)).findAll(any(Pageable.class));
        verifyNoMoreInteractions(mockProjektService);
    }
    @Test
    public void createProject_whenValidData_shouldReturnCreatedStatusWithLocation() throws Exception {
        ProjectEntity projekt = new ProjectEntity("Nazwa3", "Opis3");
        String jsonProjekt = jacksonTester.write(projekt).getJson();
        projekt.setId(3L);
        when(mockProjektService.save(any(ProjectEntity.class))).thenReturn(projekt);
        mockMvc.perform(post(apiPath).content(jsonProjekt).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString(apiPath + "/" + projekt.getId())));
    }
    @Test
    public void createProject_whenEmptyName_shouldReturnNotValidException() throws Exception {
        ProjectEntity projekt = new ProjectEntity( "", "Opis4", LocalDateTime.now());
        MvcResult result = mockMvc.perform(post(apiPath)
                        .content(jacksonTester.write(projekt).getJson())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn();
        verify(mockProjektService, times(0)).save(any(ProjectEntity.class));
        Exception exception = result.getResolvedException();
        assertNotNull(exception);
        assertTrue(exception instanceof MethodArgumentNotValidException);
        System.out.println(exception.getMessage());
    }
    @Test
    public void updateProject_whenValidData_shouldReturnOkStatus() throws Exception {
        ProjectEntity projekt = new ProjectEntity(5L, "Nazwa5", "Opis5", LocalDateTime.now());
        String jsonProjekt = jacksonTester.write(projekt).getJson();
        when(mockProjektService.findById(projekt.getId())).thenReturn(Optional.of(projekt));
        when(mockProjektService.save(any(ProjectEntity.class))).thenReturn(projekt);
        mockMvc.perform(put(apiPath + "/{id}", projekt.getId())
                        .content(jsonProjekt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andDo(print())
                .andExpect(status().isOk());
        verify(mockProjektService, times(1)).findById(projekt.getId());
        verify(mockProjektService, times(1)).save(any(ProjectEntity.class));
        verifyNoMoreInteractions(mockProjektService);
    }
    /**
     * Test sprawdza czy żądanie o danych parametrach stronicowania i sortowania
     * spowoduje przekazanie do serwisu odpowiedniego obiektu Pageable, wcześniej
     * wstrzykniętego do parametru wejściowego metody kontrolera
     */
    @Test
    public void getProjectsAndVerifyPagingParams() throws Exception {
        Integer page = 5;
        Integer size = 15;
        String sortProperty = "nazwa";
        String sortDirection = "desc";
        mockMvc.perform(get(apiPath)
                        .param("page", page.toString())
                        .param("size", size.toString())
                        .param("sort", String.format("%s,%s", sortProperty, sortDirection)))
                .andExpect(status().isOk());
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(mockProjektService, times(1)).findAll(pageableCaptor.capture());
        PageRequest pageable = (PageRequest) pageableCaptor.getValue();
        assertEquals(page, pageable.getPageNumber());
        assertEquals(size, pageable.getPageSize());
        assertEquals(sortProperty, pageable.getSort().getOrderFor(sortProperty).getProperty());
        assertEquals(Sort.Direction.DESC, pageable.getSort().getOrderFor(sortProperty).getDirection());
    }
    @BeforeEach
    public void before(TestInfo testInfo) {
        System.out.printf(" -- METODA -> %s%n", testInfo.getTestMethod().get().getName());
                ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        JacksonTester.initFields(this, mapper);
    }
    @AfterEach
    public void after(TestInfo testInfo) {
        System.out.printf("<- KONIEC -- %s%n", testInfo.getTestMethod().get().getName());
    }
}
