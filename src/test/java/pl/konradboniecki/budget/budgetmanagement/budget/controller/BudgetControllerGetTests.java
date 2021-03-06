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
import pl.konradboniecki.budget.budgetmanagement.budget.model.Budget;
import pl.konradboniecki.budget.budgetmanagement.budget.service.BudgetRepository;
import pl.konradboniecki.chassis.tools.ChassisSecurityBasicAuthHelper;

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
public class BudgetControllerGetTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BudgetRepository budgetRepository;
    @Autowired
    private ChassisSecurityBasicAuthHelper chassisSecurityBasicAuthHelper;

    private String basicAuthHeaderValue;

    @BeforeAll
    public void createBasicAuthHeader() {
        basicAuthHeaderValue = chassisSecurityBasicAuthHelper.getBasicAuthHeaderValue();
    }

    // GET /api/budgets/{id}

    @Test
    public void when_budget_is_found_by_id_param_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Long budgetId = 1L;
        Budget existingBudget = new Budget().setId(budgetId);
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(existingBudget));
        // Then:
        mockMvc.perform(get("/api/budgets/" + budgetId + "?idType=id")
                .header("Authorization", basicAuthHeaderValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void when_budget_is_found_by_id_default_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Long budgetId = 1L;
        Budget existingBudget = new Budget().setId(budgetId);
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(existingBudget));
        // Then:
        mockMvc.perform(get("/api/budgets/" + budgetId)
                .header("Authorization", basicAuthHeaderValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void when_budget_is_not_found_by_id_then_response_is_correct() throws Exception {
        // Given:
        Long budgetId = 2L;
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.empty());
        // Then:
        mockMvc.perform(get("/api/budgets/" + budgetId)
                .header("Authorization", basicAuthHeaderValue))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void when_budget_is_found_by_familyId_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Long familyId = 1L;
        Budget existingBudget = new Budget().setFamilyId(familyId);
        when(budgetRepository.findByFamilyId(familyId)).thenReturn(Optional.of(existingBudget));
        // Then:
        mockMvc.perform(get("/api/budgets/" + familyId + "?idType=family")
                .header("Authorization", basicAuthHeaderValue))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void when_budget_is_not_found_by_familyId_then_response_is_correct() throws Exception {
        // Given:
        Long familyId = 2L;
        when(budgetRepository.findByFamilyId(familyId)).thenReturn(Optional.empty());
        // Then:
        mockMvc.perform(get("/api/budgets/" + familyId + "?idType=family")
                .header("Authorization", basicAuthHeaderValue))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void when_parameter_idType_is_invalid_then_response_is_correct() throws Exception {
        mockMvc.perform(get("/api/budgets/" + 5L + "?idType=there_is_no_such_type")
                .header("Authorization", basicAuthHeaderValue))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Invalid argument idType=there_is_no_such_type")));
    }

    @Test
    public void whenBAHeaderIsMissingThenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/budgets/9000")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
