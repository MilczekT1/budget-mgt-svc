package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Optional;

import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith({SpringExtension.class})
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.MOCK,
        properties = "spring.cloud.config.enabled=false"
)
@AutoConfigureMockMvc
public class JarControllerPutTests {

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

    @Test
    public void when_jar_is_updated_then_response_is_ok() throws Exception {
        // Given:
        Long jarId = 1L;
        Jar originJar = new Jar()
                .setId(jarId)
                .setJarName("name")
                .setBudgetId(2L)
                .setCapacity(4L)
                .setCurrentAmount(3L);
        Jar jarInRequestBody = new Jar()
                .setId(jarId)
                .setJarName("modifiedName")
                .setBudgetId(1L)
                .setCapacity(5L)
                .setCurrentAmount(4L);

        // When:
        Jar mergedJar = originJar.mergeWith(jarInRequestBody);
        when(jarRepository.findByIdAndBudgetId(jarId, 1L)).thenReturn(Optional.of(originJar));
        when(jarRepository.save(any(Jar.class))).thenReturn(mergedJar);

        // Then:
        mockMvc.perform(put("/api/budgets/1/jars/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", basicAuthHeaderValue)
                .content(new ObjectMapper().writeValueAsString(jarInRequestBody)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void when_jar_is_not_found_during_update_then_response_is_ok() throws Exception {
        // Given:
        Long jarId = 1L;
        Jar jarInRequestBody = new Jar()
                .setId(jarId)
                .setJarName("modifiedName")
                .setBudgetId(1L)
                .setCapacity(5L)
                .setCurrentAmount(4L);
        // When:
        when(jarRepository.findByIdAndBudgetId(jarId, 1L)).thenReturn(Optional.empty());
        // Then:
        mockMvc.perform(put("/api/budgets/1/jars/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", basicAuthHeaderValue)
                .content(new ObjectMapper().writeValueAsString(jarInRequestBody)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void whenBAHeaderIsMissingThenUnauthorized() throws Exception {
        mockMvc.perform(put("/api/budgets/1/jars/1"))
                .andExpect(status().isUnauthorized());
    }
}
