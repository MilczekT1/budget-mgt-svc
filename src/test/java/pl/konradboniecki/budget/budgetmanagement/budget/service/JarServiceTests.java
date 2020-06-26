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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public void given_findBy_id_when_jar_found_then_returned() {
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
    public void given_findBy_id_when_jar_not_found_then_thrown() {
        // Given:
        when(jarRepository.findById(1L)).thenReturn(Optional.empty());
        // When:
        Throwable throwable = catchThrowable(() -> jarService.findByIdOrThrow(1L));
        // Then:
        assertThat(throwable).isInstanceOf(JarNotFoundException.class);
    }

    @Test
    public void given_findAll_by_budgetId_when_jars_not_found_then_returnEmpty() {
        // Given:
        when(jarRepository.findAllByBudgetId(2L)).thenReturn(Collections.EMPTY_LIST);
        // When:
        List<Jar> list = jarService.findAllJarsByBudgetId(2L);
        // Then:
        assertThat(list).isEmpty();
    }

    @Test
    public void given_findAll_by_budgetId_when_jars_found_then_returnEmpty() {
        // Given:
        List<Jar> jarList = new ArrayList<>(2);
        jarList.add(new Jar().setId(1L));
        jarList.add(new Jar().setId(2L));
        when(jarRepository.findAllByBudgetId(2L)).thenReturn(jarList);
        // When:
        List<Jar> list = jarService.findAllJarsByBudgetId(2L);
        // Then:
        assertThat(list.size()).isEqualTo(2);
    }

    @Test
    public void given_findBy_idAndBudgetId_when_jar_found_then_returned() {
        // Given:
        Jar mockedJar = new Jar()
                .setId(1L)
                .setBudgetId(1L);
        when(jarRepository.findByIdAndBudgetId(1L, 1L))
                .thenReturn(Optional.of(mockedJar));
        // When:
        Jar jar = jarService.findByIdAndBudgetIdOrThrow(1L, 1L);
        // Then:
        assertThat(jar).isEqualToComparingFieldByField(mockedJar);
    }

    @Test
    public void given_findBy_idAndBudgetId_when_jar_not_found_then_thrown() {
        // Given:
        when(jarRepository.findByIdAndBudgetId(1L, 2L))
                .thenReturn(Optional.empty());
        // When:
        Throwable throwable = catchThrowable(
                () -> jarService.findByIdAndBudgetIdOrThrow(1L, 2L));
        // Then:
        assertThat(throwable).isInstanceOf(JarNotFoundException.class);
    }

    @Test
    public void given_deleteBy_id_when_jar_found_then_do_nothing() {
        // Given:
        doNothing().when(jarRepository).deleteById(1L);
        // When:
        Throwable throwable = catchThrowable(() -> jarService.deleteJarByIdOrThrow(1L));
        // Then:
        assertThat(throwable).isNull();
    }

    @Test
    public void given_deleteBy_id_when_jar_not_found_then_throw() {
        // Given:
        doThrow(EmptyResultDataAccessException.class)
                .when(jarRepository)
                .deleteById(eq(2L));
        // When:
        Throwable throwable = catchThrowable(() -> jarService.deleteJarByIdOrThrow(2L));
        // Then:
        assertThat(throwable).isInstanceOf(JarNotFoundException.class);
    }

    @Test
    public void given_deleteBy_idAndBudgetId_when_jar_found_then_do_nothing() {
        // Given:
        doNothing().when(jarRepository).deleteByIdAndBudgetId(1L, 1L);
        // When:
        Throwable throwable = catchThrowable(() -> jarService.removeJarFromBudgetOrThrow(1L, 1L));
        // Then:
        assertThat(throwable).isNull();
    }

    @Test
    public void given_deleteBy_idAndBudgetId_when_jar_not_found_then_throw() {
        // Given:
        doThrow(EmptyResultDataAccessException.class)
                .when(jarRepository)
                .deleteByIdAndBudgetId(2L, 2L);
        // When:
        Throwable throwable = catchThrowable(() -> jarService.removeJarFromBudgetOrThrow(2L, 2L));
        // Then:
        assertThat(throwable).isInstanceOf(JarNotFoundException.class);
    }

    @Test
    public void given_different_budgetId_in_body_and_path_when_save_then_throw() {
        // Given:
        Long budgetIdFromJar = 1L;
        Long budgetIdFromPath = 2L;
        Jar jarFromBody = new Jar()
                .setBudgetId(budgetIdFromJar);
        // When:
        Throwable throwable = catchThrowable(() -> jarService.saveJar(jarFromBody, budgetIdFromPath));
        // Then:
        assertThat(throwable).isInstanceOf(JarCreationException.class);
    }

    @Test
    public void given_the_same_budgetId_in_body_and_path_when_save_then_return_jar() {
        // Given:
        Long budgetIdFromJar = 2L;
        Long budgetIdFromPath = 2L;
        Jar jarFromBody = new Jar()
                .setBudgetId(budgetIdFromJar);
        when(jarRepository.save(any(Jar.class))).thenReturn(new Jar());
        // When:
        Object jarObj = jarService.saveJar(jarFromBody, budgetIdFromPath);
        // Then:
        assertThat(jarObj).isInstanceOf(Jar.class);
    }

    @Test
    public void given_the_same_budgetId_in_body_and_path_when_update_then_return_jar() {
        // Given:
        Long budgetIdFromJar = 2L;
        Long budgetIdFromPath = 2L;
        Jar jarFromBody = new Jar()
                .setId(1L)
                .setBudgetId(budgetIdFromJar);
        when(jarRepository.findByIdAndBudgetId(1L, 2L))
                .thenReturn(Optional.of(jarFromBody));
        when(jarRepository.save(any(Jar.class))).thenReturn(jarFromBody);
        // When:
        Object jarObj = jarService.updateJar(jarFromBody.getId(), budgetIdFromPath, jarFromBody);
        // Then:
        assertThat(jarObj).isInstanceOf(Jar.class);
    }

    @Test
    public void given_different_budgetId_in_body_and_path_when_update_then_throw() {
        // Given:
        Long budgetIdFromJar = 2L;
        Long budgetIdFromPath = 1L;
        Jar jarFromBody = new Jar()
                .setId(1L)
                .setBudgetId(budgetIdFromJar);
        // When:
        Throwable throwable = catchThrowable(
                () -> jarService.updateJar(jarFromBody.getId(), budgetIdFromPath, jarFromBody));
        // Then:
        assertThat(throwable).isInstanceOf(JarNotFoundException.class);
    }
}
