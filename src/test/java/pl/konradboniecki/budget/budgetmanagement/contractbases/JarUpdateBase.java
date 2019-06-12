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
public class JarUpdateBase {

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
        mock_modify_jar_with_id_equal_1();
        mock_jar_with_id_equal_2_not_found_during_update();
    }

    private void mock_modify_jar_with_id_equal_1() {
        // Given:
        Long jarId = 1L;
        Jar originJar = new Jar()
                .setId(jarId)
                .setJarName("name")
                .setBudgetId(2L)
                .setCapacity(4L)
                .setCurrentAmount(3L);
        Jar newJar = new Jar()
                .setId(jarId)
                .setJarName("modifiedName")
                .setBudgetId(1L)
                .setCapacity(5L)
                .setCurrentAmount(4L);

        Jar mergedJar = originJar.mergeWith(newJar);
        // When:
        when(jarRepository.findByIdAndBudgetId(jarId, 1L)).thenReturn(Optional.of(originJar));
        when(jarRepository.save(any(Jar.class))).thenReturn(mergedJar);
    }

    private void mock_jar_with_id_equal_2_not_found_during_update() {
        when(jarRepository.findByIdAndBudgetId(2L, 1L))
                .thenReturn(Optional.empty());
    }
}
