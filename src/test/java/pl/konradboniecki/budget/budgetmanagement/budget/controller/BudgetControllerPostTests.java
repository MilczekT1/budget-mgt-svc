package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.persistence.PersistenceException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class BudgetControllerPostTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BudgetRepository budgetRepository;

    // POST /api/budgets

    @Test
    public void when_budget_is_created_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Budget budgetFromBody = new Budget().setFamilyId(5L);
        // When:
        when(budgetRepository.save(any(Budget.class)))
                .thenReturn(budgetFromBody.setId(1L));
        // Then:
        mockMvc.perform(post("/api/budgets")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(budgetFromBody)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void when_budget_is_not_created_then_response_is_correct() throws Exception {
        // Given:
        Budget budgetFromBody = new Budget().setFamilyId(5L);
        // When:
        doThrow(PersistenceException.class).when(budgetRepository).save(any(Budget.class));
        // Then:
        mockMvc.perform(post("/api/budgets")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(budgetFromBody)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Unexpected error occurred.")));
    }
}
