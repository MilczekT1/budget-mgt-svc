package pl.konradboniecki.budget.budgetmanagement.contractbases;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseRepository;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseService;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "spring.cloud.config.enabled=false"
)
public class ExpenseDeleteBase {

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

        mock_expense_found_by_id_equal_1_during_deletion();
        mock_expense_not_found_by_id_equal_2_during_deletion();
    }

    private void mock_expense_found_by_id_equal_1_during_deletion() {
        doNothing().when(expenseRepository)
                .deleteByIdAndBudgetId(eq(1L), eq(1L));
    }

    private void mock_expense_not_found_by_id_equal_2_during_deletion() {
        doThrow(EmptyResultDataAccessException.class)
                .when(expenseRepository).deleteByIdAndBudgetId(eq(2L), eq(1L));
    }
}
