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
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarRepository;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarService;

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
public abstract class JarDeleteBase {

    @Autowired
    protected JarService jarService;
    @MockBean
    protected JarRepository jarRepository;
    @LocalServerPort
    int port;

    @Before
    public void setUpMocks() {
        RestAssured.baseURI = "http://localhost:" + this.port;
        RestAssured.config = config().redirect(redirectConfig().followRedirects(false));

        mock_jar_found_by_id_equal_1_during_deletion();
        mock_jar_not_found_by_id_equal_2_during_deletion();
    }

    private void mock_jar_found_by_id_equal_1_during_deletion() {
        doNothing().when(jarRepository).deleteById(eq(1L));
    }

    private void mock_jar_not_found_by_id_equal_2_during_deletion() {
        doThrow(EmptyResultDataAccessException.class)
                .when(jarRepository).deleteByIdAndBudgetId(eq(2L), eq(1L));
    }
}
