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

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.MOCK,
        properties = "spring.cloud.config.enabled=false"
)
@AutoConfigureMockMvc
public class ExpenseControllerPostTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExpenseRepository expenseRepository;

    // POST /api/budgets/{budgetId}/expenses

    @Test
    public void when_expense_is_created_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Expense expenseInRequestBody = new Expense()
                .setLabelId(1L)
                .setBudgetId(1L)
                .setComment("comment")
                .setAmount(1L);
        Expense expense = new Expense()
                .setLabelId(1L)
                .setBudgetId(1L)
                .setComment("comment")
                .setAmount(1L)
                .setId(1L)
                .setExpenseDate(ZonedDateTime.now(ZoneId.of("UTC")));
        when(expenseRepository.save(expenseInRequestBody))
                .thenReturn(expense);
        // Then:
        mockMvc.perform(post("/api/budgets/1/expenses")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(expenseInRequestBody)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString()));
    }

    @Test
    public void when_budgetId_does_not_match_then_response_status_and_headers_are_ok() throws Exception {
        // Given:
        Expense expenseInRequestBody = new Expense()
                .setLabelId(1L)
                .setBudgetId(1L)
                .setComment("comment")
                .setAmount(1L);

        // Then:
        mockMvc.perform(post("/api/budgets/2/expenses")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(expenseInRequestBody)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString()))
                .andExpect(content().string(containsString("Budget id in body and path don't match.")));

    }
}
