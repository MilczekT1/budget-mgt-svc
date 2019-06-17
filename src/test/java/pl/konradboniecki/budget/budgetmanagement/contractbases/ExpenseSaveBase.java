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

import java.time.ZonedDateTime;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "spring.cloud.config.enabled=false"
)
public class ExpenseSaveBase {

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
        mock_creation_success_with_all_properties();
        mock_creation_success_with_only_mandatory_properties();
    }

    public void mock_creation_success_with_all_properties(){
        Expense mockedExpenseInRequestBody = new Expense()
                .setBudgetId(1L)
                .setLabelId(1L)
                .setComment("comment")
                .setAmount(5L)
                .setId(null)
                .setExpenseDate(null);
        Expense mockedExpenseInResponseBody = new Expense()
                .setBudgetId(1L)
                .setLabelId(1L)
                .setComment("comment")
                .setAmount(5L)
                .setId(1L)
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:22:54.246625Z"));

        when(expenseRepository.save(mockedExpenseInRequestBody))
                .thenReturn(mockedExpenseInResponseBody);
    }

    public void mock_creation_success_with_only_mandatory_properties(){
        Expense mockedExpenseInRequestBody = new Expense()
                .setBudgetId(1L)
                .setAmount(5L);

        Expense mockedExpenseInResponseBody = new Expense()
                .setBudgetId(1L)
                .setAmount(5L)
                .setLabelId(null)
                .setComment(null)
                .setId(1L)
                .setExpenseDate(ZonedDateTime.parse("2019-06-16T10:22:54.246625Z"));

        when(expenseRepository.save(mockedExpenseInRequestBody))
                .thenReturn(mockedExpenseInResponseBody);
    }
}
