package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import org.junit.jupiter.api.Test;
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

import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    // GET /api/budgets/{id}

    @Test
    public void when_budget_is_found_by_id_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Long budgetId = 1L;
        Budget existingBudget = new Budget().setId(budgetId);
        when(budgetRepository.findById(budgetId)).thenReturn(Optional.of(existingBudget));
        // Then:
        mockMvc.perform(get("/api/budgets/" + budgetId))
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
        mockMvc.perform(get("/api/budgets/" + budgetId))
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
        mockMvc.perform(get("/api/budgets/" + familyId + "?idType=family"))
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
        mockMvc.perform(get("/api/budgets/" + familyId + "?idType=family"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void when_parameter_idType_is_invalid_then_response_is_correct() throws Exception {
        mockMvc.perform(get("/api/budgets/" + 5L + "?idType=there_is_no_such_type"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Invalid argument idType=there_is_no_such_type")));
    }
}
