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
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.MOCK,
        properties = "spring.cloud.config.enabled=false"
)
@AutoConfigureMockMvc
public class ExpenseControllerGetTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExpenseRepository expenseRepository;

    // GET /api/budgets/{id}/expenses/{expenseId}

    @Test
    public void when_expense_not_found_then_response_is_correct() throws Exception {
        // Given:
        when(expenseRepository.findByIdAndBudgetId(1L, 1L))
                .thenReturn(Optional.empty());
        // Then:
        mockMvc.perform(get("/api/budgets/1/expenses/1"))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString()))
                .andExpect(content().string(containsString("Expense with id: 1 not found in budget with id: 1.")));

    }

    @Test
    public void when_expense_is_found_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Expense mockedExpense = new Expense()
                .setAmount(5L)
                .setBudgetId(1L)
                .setId(1L)
                .setComment("testComment")
                .setLabelId(1L);
        when(expenseRepository.findByIdAndBudgetId(1L,1L))
                .thenReturn(Optional.of(mockedExpense));
        // Then:
        mockMvc.perform(get("/api/budgets/1/expenses/1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString()));

    }

    // GET /api/budgets/{id}/expenses

    @Test
    public void when_expenses_are_found_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Expense firstExpense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setLabelId(1L)
                .setAmount(3L)
                .setComment("test_comments_1")
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:22:54.246625Z"));
        Expense secondExpense = new Expense()
                .setId(2L)
                .setBudgetId(1L)
                .setLabelId(1L)
                .setAmount(4L)
                .setComment("test_comments_2")
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:28:23.053553Z"));
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(firstExpense);
        expenseList.add(secondExpense);
        when(expenseRepository.findAllByBudgetId(1L))
                .thenReturn(expenseList);
        // Then:
        mockMvc.perform(get("/api/budgets/1/expenses"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString()));
    }
}
