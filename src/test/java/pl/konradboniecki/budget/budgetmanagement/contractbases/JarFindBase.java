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

import java.util.ArrayList;
import java.util.Optional;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.RANDOM_PORT,
        properties = "spring.cloud.config.enabled=false"
)
public abstract class JarFindBase {

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

        mock_jar_found_by_id_equal_1();
        mock_jar_not_found_by_id_equal_2();
        mock_get_2_jars_as_list();
    }

    private void mock_jar_found_by_id_equal_1(){
        Jar jar = new Jar()
                .setId(1L)
                .setBudgetId(1L)
                .setCapacity(50L)
                .setCurrentAmount(20L)
                .setJarName("testJarName");
        when(jarRepository.findByIdAndBudgetId(eq(1L), eq(1L)))
                .thenReturn(Optional.of(jar));
    }

    private void mock_jar_not_found_by_id_equal_2(){
        when(jarRepository.findByIdAndBudgetId(eq(1L), eq(2L)))
                .thenReturn(Optional.empty());
    }

    private void mock_get_2_jars_as_list(){
        Jar firstJar = new Jar()
                .setJarName("name1")
                .setBudgetId(1L)
                .setId(1L)
                .setCurrentAmount(0L)
                .setCapacity(3L);
        Jar secondJar = new Jar()
                .setJarName("name2")
                .setBudgetId(1L)
                .setId(2L)
                .setCurrentAmount(0L)
                .setCapacity(3L);
        ArrayList<Jar> jarList = new ArrayList<>();
        jarList.add(firstJar);
        jarList.add(secondJar);
        when(jarRepository.findAllByBudgetId(1L)).thenReturn(jarList);
    }
}
