package pl.konradboniecki.budget.budgetmanagement.contractbases;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseRepository;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "spring.cloud.config.enabled=false"
)
public class ExpenseUpdateBase {

    @Autowired
    protected ExpenseService expenseService;
    @MockBean
    protected ExpenseRepository expenseRepository;
    @LocalServerPort
    int port;

    @Before
    public void setUpMocks() {
        RestAssured.baseURI = "http://localhost:" + this.port;
        RestAssured.config = config().redirect(redirectConfig().followRedirects(false));
        mock_modify_expense_with_id_equal_1();
        mock_expense_with_id_equal_2_not_found_during_update();
    }

    private void mock_modify_expense_with_id_equal_1() {
        // Given:
        Long expenseId = 1L;
        Expense originExpense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setLabelId(1L)
                .setComment("comment")
                .setAmount(1L)
                .setExpenseDate(ZonedDateTime.now(ZoneId.of("UTC")));
        Expense newExpense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setLabelId(1L)
                .setComment("edited_comment")
                .setAmount(1L)
                .setExpenseDate(ZonedDateTime.now(ZoneId.of("UTC")));

        Expense mergedExpense = originExpense.mergeWith(newExpense);
        // When:
        when(expenseRepository.findByIdAndBudgetId(expenseId, 1L)).thenReturn(Optional.of(originExpense));
        when(expenseRepository.save(any(Expense.class))).thenReturn(mergedExpense);
    }

    private void mock_expense_with_id_equal_2_not_found_during_update() {
        when(expenseRepository.findByIdAndBudgetId(2L, 1L))
                .thenReturn(Optional.empty());
    }
}
