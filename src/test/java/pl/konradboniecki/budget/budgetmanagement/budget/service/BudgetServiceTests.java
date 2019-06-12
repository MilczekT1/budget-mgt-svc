package pl.konradboniecki.budget.budgetmanagement.budget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Budget;
import pl.konradboniecki.chassis.exceptions.BadRequestException;
import pl.konradboniecki.chassis.exceptions.InternalServerErrorException;

import javax.persistence.PersistenceException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.NONE,
        properties = "spring.cloud.config.enabled=false"
)
public class BudgetServiceTests {

    @MockBean
    private BudgetRepository budgetRepository;
    @Autowired
    private BudgetService budgetService;

    @Test
    public void given_findBy_id_when_budget_not_found_then_throw() {
        // Given:
        when(budgetRepository.findById(1L)).thenReturn(Optional.empty());
        // When:
        Throwable throwable =  catchThrowable(() -> budgetService.findByOrThrow(1L, "id"));
        // Then:
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(BudgetNotFoundException.class);
    }

    @Test
    public void given_findBy_id_when_budget_found_then_return_budget() {
        // Given:
        Long id = 1L;
        Budget bgt = new Budget().setId(id);
        when(budgetRepository.findById(id)).thenReturn(Optional.of(bgt));
        // When:
        Budget retrievedBudget = budgetService.findByOrThrow(id, "id");
        // Then:
        assertThat(retrievedBudget).isNotNull();
        assertThat(retrievedBudget.getId()).isEqualTo(id);
    }

    @Test
    public void given_findBy_familyId_when_budget_found_then_return_budget() {
        // Given:
        Long familyId = 2L;
        Budget bgt = new Budget().setFamilyId(familyId);
        when(budgetRepository.findByFamilyId(familyId))
                .thenReturn(Optional.of(bgt));
        // When:
        Budget retrievedBudget = budgetService.findByOrThrow(familyId, "family");
        // Then:
        assertThat(retrievedBudget).isEqualToComparingFieldByField(bgt);
    }

    @Test
    public void given_findBy_familyId_when_budget_not_found_then_throw() {
        // Given:
        Long familyId = 3L;
        when(budgetRepository.findByFamilyId(familyId))
                .thenReturn(Optional.empty());
        // When:
        Throwable throwable =  catchThrowable(() -> budgetService.findByOrThrow(familyId, "family"));
        // Then:
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(BudgetNotFoundException.class);
    }

    @Test
    public void when_findBy_invalid_IdType_then_throw() {
        // Given:
        String invalidIdType = "invalidIdType";
        // When:
        Throwable throwable =  catchThrowable(() -> budgetService.findByOrThrow(10L, invalidIdType));
        // Then:
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(BadRequestException.class);
    }

    @Test
    public void given_save_budget_when_no_conflict_then_return_budget() {
        // Given:
        Budget budgetWithoutConflict = new Budget().setId(2L);
        when(budgetRepository.save(same(budgetWithoutConflict))).thenReturn(budgetWithoutConflict);
        // When:
        Budget retrievedBudget = budgetService.saveBudget(budgetWithoutConflict);
        // Then:
        assertThat(retrievedBudget).isEqualToComparingFieldByField(budgetWithoutConflict);

    }

    @Test
    public void given_save_budget_when_conflict_then_throw() {
        // Given:
        Budget budgetWithConflict = new Budget().setId(1L);
        doThrow(PersistenceException.class).when(budgetRepository).save(same(budgetWithConflict));
        // When:
        Throwable throwable =  catchThrowable(() -> budgetService.saveBudget(budgetWithConflict));
        // Then:
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(InternalServerErrorException.class);
    }
}
