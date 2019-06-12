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

import java.util.Optional;

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
public abstract class BudgetFindBase {

    @Autowired
    protected BudgetService budgetService;
    @MockBean
    protected BudgetRepository budgetRepository;
    @LocalServerPort
    int port;

    @Before
    public void setUpMocks() {
        RestAssured.baseURI = "http://localhost:" + this.port;
        RestAssured.config = config().redirect(redirectConfig().followRedirects(false));

        mock_budget_found_by_family_id_equal_1();
        mock_budget_found_by_id_equal_1();
        mock_budget_not_found_by_family_id_equal_100();
        mock_budget_not_found_by_id_equal_100();
    }

    private void mock_budget_found_by_family_id_equal_1() {
        Budget budget = new Budget()
                .setFamilyId(1L)
                .setId(2L);
        when(budgetRepository.findByFamilyId(1L))
                .thenReturn(Optional.of(budget));
    }

    private void mock_budget_not_found_by_id_equal_100() {
        when(budgetRepository.findById(100L))
                .thenReturn(Optional.empty());
    }

    private void mock_budget_not_found_by_family_id_equal_100() {
        when(budgetRepository.findByFamilyId(100L))
                .thenReturn(Optional.empty());
    }

    private void mock_budget_found_by_id_equal_1() {
        Budget budget2 = new Budget()
                .setFamilyId(2L)
                .setId(1L);
        when(budgetRepository.findById(1L))
                .thenReturn(Optional.of(budget2));
    }
}
