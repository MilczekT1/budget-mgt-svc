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
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class ExpenseControllerPutTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExpenseRepository expenseRepository;

    @Test
    public void when_expense_is_updated_then_response_is_ok() throws Exception {
        // Given:
        Long expenseId = 1L;
        Expense originExpense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setComment("comment")
                .setAmount(1L);
        Expense expenseInRequestBody = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setComment("edited_comment")
                .setAmount(1L);

        Expense mergedExpense = originExpense.mergeWith(expenseInRequestBody);
        // When:
        when(expenseRepository.findByIdAndBudgetId(expenseId, 1L)).thenReturn(Optional.of(originExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(mergedExpense);

        // Then:
        mockMvc.perform(put("/api/budgets/1/expenses/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(expenseInRequestBody)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void when_expense_is_not_found_during_update_then_response_is_ok() throws Exception {
        // Given:
        Long expenseId = 1L;
        Expense expenseInRequestBody = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setComment("edited_comment")
                .setAmount(1L);
        // When:
        when(expenseRepository.findByIdAndBudgetId(expenseId, 1L)).thenReturn(Optional.empty());
        // Then:
        mockMvc.perform(put("/api/budgets/1/expenses/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(expenseInRequestBody)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
