package pl.konradboniecki.budget.budgetmanagement.budget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Jar;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.NONE,
        properties = "spring.cloud.config.enabled=false"
)
public class JarServiceTests {

    @MockBean
    private JarRepository jarRepository;
    @Autowired
    private JarService jarService;

    @Test
    public void given_findBy_id_when_jar_found_then_returned(){
        // Given:
        Jar mockedJar = new Jar().setId(1L);
        when(jarRepository.findById(1L))
                .thenReturn(Optional.of(mockedJar));
        // When:
        Jar jar = jarService.findByIdOrThrow(1L);
        // Then:
        assertThat(jar).isEqualToComparingFieldByField(mockedJar);
    }

    @Test
    public void given_findBy_id_when_jar_not_found_then_thrown(){
        // Given:
        when(jarRepository.findById(1L)).thenReturn(Optional.empty());
        // When:
        Throwable throwable = catchThrowable(()->jarService.findByIdOrThrow(1L));
        // Then:
        assertThat(throwable).isInstanceOf(JarNotFoundException.class);
    }

    @Test
    public void given_deleteBy_id_when_jar_found_then_do_nothing(){
        // Given:
        doNothing().when(jarRepository).deleteById(1L);
        // When:
        Throwable throwable = catchThrowable(() -> jarService.deleteJarByIdOrThrow(1L));
        // Then:
        assertThat(throwable).isNull();
    }
    @Test
    public void given_deleteBy_id_when_jar_not_found_then_throw(){
        // Given:
        doThrow(EmptyResultDataAccessException.class)
                .when(jarRepository)
                .deleteById(eq(2L));
        // When:
        Throwable throwable = catchThrowable(() -> jarService.deleteJarByIdOrThrow(2L));
        // Then:
        assertThat(throwable).isInstanceOf(JarNotFoundException.class);
    }
}
