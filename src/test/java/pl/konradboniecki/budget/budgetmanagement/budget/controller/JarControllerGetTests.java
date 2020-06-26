package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Jar;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarRepository;
import pl.konradboniecki.chassis.tools.ChassisSecurityBasicAuthHelper;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.MOCK,
        properties = "spring.cloud.config.enabled=false"
)
@AutoConfigureMockMvc
public class JarControllerGetTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JarRepository jarRepository;
    @Autowired
    private ChassisSecurityBasicAuthHelper chassisSecurityBasicAuthHelper;

    private String basicAuthHeaderValue;

    @BeforeAll
    public void createBasicAuthHeader() {
        basicAuthHeaderValue = chassisSecurityBasicAuthHelper.getBasicAuthHeaderValue();
    }

    // GET /api/budgets/{budgetId}/jars/{id}

    @Test
    public void when_jar_not_found_then_response_is_correct() throws Exception {
        // Given:
        when(jarRepository.findByIdAndBudgetId(1L, 1L))
                .thenReturn(Optional.empty());
        // Then:
        mockMvc.perform(get("/api/budgets/1/jars/1")
                .header("Authorization", basicAuthHeaderValue))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Jar with id: 1 not found in budget with id: 1")));

    }

    @Test
    public void when_jar_is_found_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Jar mockedJar = new Jar()
                .setCapacity(1L)
                .setCurrentAmount(1L)
                .setId(1L)
                .setJarName("testName")
                .setBudgetId(1L);
        when(jarRepository.findByIdAndBudgetId(1L, 1L))
                .thenReturn(Optional.of(mockedJar));
        // Then:
        mockMvc.perform(get("/api/budgets/1/jars/1")
                .header("Authorization", basicAuthHeaderValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void when_jars_are_found_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Jar firstJar = new Jar()
                .setJarName("name1")
                .setBudgetId(1L)
                .setId(1L)
                .setCurrentAmount(0L)
                .setCapacity(3L);
        Jar secondJar = new Jar()
                .setJarName("name2")
                .setBudgetId(1L)
                .setId(2L)
                .setCurrentAmount(0L)
                .setCapacity(3L);
        ArrayList<Jar> jarList = new ArrayList<>();
        jarList.add(firstJar);
        jarList.add(secondJar);
        when(jarRepository.findAllByBudgetId(1L)).thenReturn(jarList);
        // Then:
        mockMvc.perform(get("/api/budgets/1/jars")
                .header("Authorization", basicAuthHeaderValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenBAHeaderIsMissingThenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/budgets/1/jars/1"))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get("/api/budgets/1/jars"))
                .andExpect(status().isUnauthorized());
    }
}
