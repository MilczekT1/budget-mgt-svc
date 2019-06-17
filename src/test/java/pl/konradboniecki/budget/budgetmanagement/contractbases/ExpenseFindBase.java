package pl.konradboniecki.budget.budgetmanagement.contractbases;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseRepository;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "spring.cloud.config.enabled=false"
)
public class ExpenseFindBase {

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

        mock_expense_found();
        mock_get_2_expenses_as_list();
    }

    private void mock_expense_found(){
        Expense expense = new Expense()
                .setAmount(1L)
                .setComment("test_comment")
                .setLabelId(2L)
                .setBudgetId(1L)
                .setId(1L)
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:22:54.246625Z"));
        when(expenseRepository.findByIdAndBudgetId(1L,1L)).thenReturn(Optional.of(expense));
    }

    private void mock_get_2_expenses_as_list() {
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
    }
}
