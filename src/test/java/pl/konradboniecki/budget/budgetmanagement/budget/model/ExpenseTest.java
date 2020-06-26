package pl.konradboniecki.budget.budgetmanagement.budget.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class ExpenseTest {

    @Test
    public void merge_populates_all_properties_if_not_null() {
        // Given:
        Expense expense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setComment("1")
                .setAmount(1L)
                .setCreated(Instant.now());
        Expense expenseWithSetProperties = new Expense()
                .setId(2L)
                .setBudgetId(2L)
                .setComment("2")
                .setAmount(2L)
                .setCreated(Instant.now());
        // When:
        Expense mergedExpense = expense.mergeWith(expenseWithSetProperties);
        // Then:
        Assertions.assertAll(
                () -> assertThat(mergedExpense.getId()).isEqualTo(expenseWithSetProperties.getId()),
                () -> assertThat(mergedExpense.getBudgetId()).isEqualTo(expenseWithSetProperties.getBudgetId()),
                () -> assertThat(mergedExpense.getComment()).isEqualTo(expenseWithSetProperties.getComment()),
                () -> assertThat(mergedExpense.getAmount()).isEqualTo(expenseWithSetProperties.getAmount()),
                () -> assertThat(mergedExpense.getCreated()).isEqualTo(expenseWithSetProperties.getCreated())
        );
    }

    @Test
    public void merge_does_not_populate_properties_if_null() {
        // Given:
        Expense expense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setComment("1")
                .setAmount(1L)
                .setCreated(Instant.now());
        // When:
        Expense mergedExpense = expense.mergeWith(new Expense());
        // Then:
        Assertions.assertAll(
                () -> assertThat(mergedExpense.getId()).isEqualTo(1L),
                () -> assertThat(mergedExpense.getBudgetId()).isEqualTo(1L),
                () -> assertThat(mergedExpense.getComment()).isEqualTo("1"),
                () -> assertThat(mergedExpense.getAmount()).isEqualTo(1L),
                () -> assertThat(mergedExpense.getCreated()).isEqualTo(expense.getCreated())
        );
    }

    @Test
    public void when_merge_with_null_then_throw_npe() {
        // When:
        Throwable npe = catchThrowable(() -> new Expense().mergeWith(null));
        // Then:
        assertThat(npe).isInstanceOf(NullPointerException.class);
    }
}
