package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseRepository;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
public class ExpenseControllerDeleteTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExpenseRepository expenseRepository;

    // DELETE /api/budgets/{budgetId}/expenses/{id}

    @Test
    public void when_expense_found_during_deletion_then_response_is_correct() throws Exception {
        // Given:
        doNothing().when(expenseRepository).deleteByIdAndBudgetId(1L, 1L);

        // Then:
        mockMvc.perform(delete("/api/budgets/1/expenses/1"))
                .andExpect(status().isNoContent());

    }

    @Test
    public void when_expense_not_found_during_deletion_then_response_is_correct() throws Exception {
        // Given:
        doThrow(EmptyResultDataAccessException.class)
                .when(expenseRepository).deleteByIdAndBudgetId(1L, 1L);

        // Then:
        mockMvc.perform(delete("/api/budgets/1/expenses/1"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().string(containsString("Expense with id: 1 not found in budget with id: 1.")));
    }
}
