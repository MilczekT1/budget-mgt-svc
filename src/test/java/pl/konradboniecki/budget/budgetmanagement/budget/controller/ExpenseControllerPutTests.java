package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.assertj.core.api.Assertions;
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
import org.springframework.test.web.servlet.MvcResult;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseRepository;
import pl.konradboniecki.chassis.exceptions.ErrorDescription;
import pl.konradboniecki.chassis.tools.ChassisSecurityBasicAuthHelper;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
public class ExpenseControllerPutTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExpenseRepository expenseRepository;
    @Autowired
    private ChassisSecurityBasicAuthHelper chassisSecurityBasicAuthHelper;

    private String basicAuthHeaderValue;

    @BeforeAll
    public void createBasicAuthHeader() {
        basicAuthHeaderValue = chassisSecurityBasicAuthHelper.getBasicAuthHeaderValue();
    }

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
        MvcResult mvcResult = mockMvc.perform(put("/api/budgets/1/expenses/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", basicAuthHeaderValue)
                .content(new ObjectMapper().writeValueAsString(expenseInRequestBody)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // And:
        String responseBody = mvcResult.getResponse().getContentAsString();
        Expense returnedExpense = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(responseBody, Expense.class);
        Assertions.assertThat(returnedExpense).isNotNull();
        Assertions.assertThat(returnedExpense).isEqualTo(mergedExpense);
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
        MvcResult mvcResult = mockMvc.perform(put("/api/budgets/1/expenses/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", basicAuthHeaderValue)
                .content(new ObjectMapper().writeValueAsString(expenseInRequestBody)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        // And:
        String responseBody = mvcResult.getResponse().getContentAsString();
        ErrorDescription errorDescription = new ObjectMapper().registerModule(new JavaTimeModule())
                .readValue(responseBody, ErrorDescription.class);
        Assertions.assertThat(errorDescription).isNotNull();
        org.junit.jupiter.api.Assertions.assertAll(
                () -> assertThat(errorDescription.getStatus()).isEqualTo(404),
                () -> assertThat(errorDescription.getMessage()).isEqualTo("Expense with id: 1 not found in budget with id: 1."),
                () -> assertThat(errorDescription.getStatusName()).isEqualTo("NOT_FOUND"),
                () -> assertThat(errorDescription.getTimestamp()).isInstanceOf(Instant.class)
        );
    }

    @Test
    public void whenBAHeaderIsMissingThenUnauthorized() throws Exception {
        mockMvc.perform(put("/api/budgets/1/expenses/1"))
                .andExpect(status().isUnauthorized());
    }

}
