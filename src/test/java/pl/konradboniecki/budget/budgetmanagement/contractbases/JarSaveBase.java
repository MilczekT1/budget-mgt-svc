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
import pl.konradboniecki.budget.budgetmanagement.budget.model.Jar;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarRepository;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarService;

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
public class JarSaveBase {

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
        mock_create_jar_with_id_equal_1();
    }

    private void mock_create_jar_with_id_equal_1() {
        Jar jar = new Jar()
                .setJarName("name")
                .setBudgetId(1L)
                .setCapacity(5L);

        Jar jar2 = new Jar()
                .setJarName("name")
                .setBudgetId(1L)
                .setCapacity(5L)
                .setCurrentAmount(0L)
                .setId(1L);

        when(jarRepository.save(jar)).thenReturn(jar2);
    }
}
