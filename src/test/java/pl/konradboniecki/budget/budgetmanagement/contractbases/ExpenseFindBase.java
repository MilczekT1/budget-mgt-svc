package pl.konradboniecki.budget.budgetmanagement.contractbases;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import static org.mockito.ArgumentMatchers.eq;
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

    private Expense firstExpense;
    private Expense secondExpense;

    @Before
    public void setUpMocks() {
        RestAssured.baseURI = "http://localhost:" + this.port;
        RestAssured.config = config().redirect(redirectConfig().followRedirects(false));
        setUpExpenses();
        mock_expense_found();
        mock_get_2_expenses_in_page_with_default_pagination();
        mock_get_first_expense__in_page_with_limit_equal_1();
        mock_get_second_expense_in_page_with_offset_equal_1();
    }

    private void setUpExpenses() {
        firstExpense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setAmount(3L)
                .setComment("test_comments_1")
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:22:54.246625Z"));
        secondExpense = new Expense()
                .setId(2L)
                .setBudgetId(1L)
                .setAmount(4L)
                .setComment("test_comments_2")
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:28:23.053553Z"));
    }

    private void mock_expense_found() {
        Expense expense = new Expense()
                .setAmount(1L)
                .setComment("test_comment")
                .setBudgetId(1L)
                .setId(1L)
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:22:54.246625Z"));
        when(expenseRepository.findByIdAndBudgetId(1L, 1L)).thenReturn(Optional.of(expense));
    }

    private void mock_get_2_expenses_in_page_with_default_pagination() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(firstExpense);
        expenseList.add(secondExpense);
        Pageable pageable = PageRequest.of(0, 100);
        Page<Expense> page = new PageImpl<>(expenseList, pageable, 2);

        when(expenseRepository.findAllByBudgetId(eq(1L), eq(pageable)))
                .thenReturn(page);
    }

    private void mock_get_first_expense__in_page_with_limit_equal_1() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(firstExpense);
        Pageable pageable = PageRequest.of(0, 1);
        Page<Expense> page = new PageImpl<>(expenseList, pageable, 2);

        when(expenseRepository.findAllByBudgetId(eq(1L), eq(pageable)))
                .thenReturn(page);
    }

    private void mock_get_second_expense_in_page_with_offset_equal_1() {
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(secondExpense);
        Pageable pageable = PageRequest.of(1, 100);
        Page<Expense> page = new PageImpl<>(expenseList, pageable, 2);

        when(expenseRepository.findAllByBudgetId(eq(1L), eq(pageable)))
                .thenReturn(page);
    }
}
