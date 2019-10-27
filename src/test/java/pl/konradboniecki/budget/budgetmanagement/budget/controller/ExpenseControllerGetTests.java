package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseRepository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Expense with id: 1 not found in budget with id: 1.")));

    }

    @Test
    public void when_expense_found_then_response_status_and_headers_are_correct() throws Exception {
        // Given:
        Expense mockedExpense = new Expense()
                .setAmount(5L)
                .setBudgetId(1L)
                .setId(1L)
                .setComment("testComment");
        when(expenseRepository.findByIdAndBudgetId(1L, 1L))
                .thenReturn(Optional.of(mockedExpense));
        // Then:
        mockMvc.perform(get("/api/budgets/1/expenses/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"id\":1,\"budgetId\":1,\"amount\":5,\"comment\":\"testComment\",\"expenseDate\":null}"));

    }

    // GET /api/budgets/{id}/expenses

    @Test
    public void when_expenses_found_with_default_params_then_response_is_correct() throws Exception {
        // Given:
        Expense firstExpense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setAmount(3L)
                .setComment("test_comments_1")
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:22:54.246625Z"));
        Expense secondExpense = new Expense()
                .setId(2L)
                .setBudgetId(1L)
                .setAmount(4L)
                .setComment("test_comments_2")
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:28:23.053553Z"));
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(firstExpense);
        expenseList.add(secondExpense);
        Pageable pageable = PageRequest.of(0, 100);
        Page<Expense> page = new PageImpl<>(expenseList, pageable, 2);

        when(expenseRepository.findAllByBudgetId(eq(1L), any(Pageable.class)))
                .thenReturn(page);
        // Then:
        MvcResult mvcResult = mockMvc.perform(get("/api/budgets/1/expenses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        boolean responseBodyIsCorrect = Pattern.matches("\\{\"items\":\\[.*{1,}\\],\"_meta\":\\{\"page\":0,\"pageSize\":100,\"totalPages\":1,\"elements\":2,\"totalElements\":2\\}\\}", mvcResult.getResponse().getContentAsString().trim());
        assertThat(responseBodyIsCorrect).isTrue();
    }

    @Test
    public void when_expenses_found_with_limit_1_then_1_expense_is_visible() throws Exception {
        // Given:
        Expense firstExpense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setAmount(3L)
                .setComment("test_comments_1")
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:22:54.246625Z"));
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(firstExpense);
        Pageable pageable = PageRequest.of(0, 1);
        Page<Expense> page = new PageImpl<>(expenseList, pageable, 2);

        when(expenseRepository.findAllByBudgetId(eq(1L), any(Pageable.class)))
                .thenReturn(page);
        // Then:
        MvcResult mvcResult = mockMvc.perform(get("/api/budgets/1/expenses?limit=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString().trim();
        boolean paginationInBodyIsCorrect = Pattern.matches(".*,\"_meta\":\\{\"page\":0,\"pageSize\":1,\"totalPages\":2,\"elements\":1,\"totalElements\":2\\}\\}", responseBodyAsString);
        boolean onlyOneExpenseIsVisible = Pattern.matches("\\{\"items\":\\[.*test_comments_1.*\\],.*", responseBodyAsString);
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(paginationInBodyIsCorrect).isTrue(),
                () -> assertThat(onlyOneExpenseIsVisible).isTrue()
        );
    }

    @Test
    public void when_expenses_found_with_page_1_then_pagination_medatada_is_correct() throws Exception {
        // Given:
        Expense firstExpense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setAmount(3L)
                .setComment("test_comments_1")
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:22:54.246625Z"));
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(firstExpense);
        Pageable pageable = PageRequest.of(1, 100);
        Page<Expense> page = new PageImpl<>(expenseList, pageable, 101);

        when(expenseRepository.findAllByBudgetId(eq(1L), any(Pageable.class)))
                .thenReturn(page);
        // Then:
        MvcResult mvcResult = mockMvc.perform(get("/api/budgets/1/expenses?page=1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseBodyAsString = mvcResult.getResponse().getContentAsString().trim();
        boolean paginationInBodyIsCorrect = Pattern.matches(".*,\"_meta\":\\{\"page\":1,\"pageSize\":100,\"totalPages\":2,\"elements\":1,\"totalElements\":101\\}\\}", responseBodyAsString);
        boolean onlyOneExpenseIsVisible = Pattern.matches("\\{\"items\":\\[.*test_comments_1.*\\],.*", responseBodyAsString);
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(paginationInBodyIsCorrect).isTrue(),
                () -> assertThat(onlyOneExpenseIsVisible).isTrue()
        );
    }
}
