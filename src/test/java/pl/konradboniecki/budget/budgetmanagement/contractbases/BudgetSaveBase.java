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
import pl.konradboniecki.budget.budgetmanagement.budget.model.Budget;
import pl.konradboniecki.budget.budgetmanagement.budget.service.BudgetRepository;
import pl.konradboniecki.budget.budgetmanagement.budget.service.BudgetService;

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
public abstract class BudgetSaveBase {

    @Autowired protected BudgetService budgetService;
    @MockBean protected BudgetRepository budgetRepository;
    @LocalServerPort int port;

    @Before
    public void setUpMocks() {
        RestAssured.baseURI = "http://localhost:" + this.port;
        RestAssured.config = config().redirect(redirectConfig().followRedirects(false));
        mock_budget_created_with_id_equal_5();
    }

    private void mock_budget_created_with_id_equal_5(){
        Budget budget = new Budget()
                .setFamilyId(5L);
        when(budgetRepository.save(any(Budget.class)))
                .thenReturn(budget.setId(5L));
    }
}
