package pl.konradboniecki.budget.budgetmanagement.budget.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
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
                .setExpenseDate(ZonedDateTime.now());
        Expense expenseWithSetProperties = new Expense()
                .setId(2L)
                .setBudgetId(2L)
                .setComment("2")
                .setAmount(2L)
                .setExpenseDate(ZonedDateTime.now());
        // When:
        Expense mergedExpense = expense.mergeWith(expenseWithSetProperties);
        // Then:
        Assertions.assertAll(
                () -> assertThat(mergedExpense.getId()).isEqualTo(expenseWithSetProperties.getId()),
                () -> assertThat(mergedExpense.getBudgetId()).isEqualTo(expenseWithSetProperties.getBudgetId()),
                () -> assertThat(mergedExpense.getComment()).isEqualTo(expenseWithSetProperties.getComment()),
                () -> assertThat(mergedExpense.getAmount()).isEqualTo(expenseWithSetProperties.getAmount()),
                () -> assertThat(mergedExpense.getExpenseDate()).isEqualTo(expenseWithSetProperties.getExpenseDate())
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
                .setExpenseDate(ZonedDateTime.now());
        Expense expenseWitNullProperties = new Expense()
                .setId(2L)
                .setBudgetId(2L)
                .setComment("2")
                .setAmount(2L)
                .setExpenseDate(ZonedDateTime.now());
        // When:
        Expense mergedExpense = expense.mergeWith(expenseWitNullProperties);
        // Then:
        Assertions.assertAll(
                () -> assertThat(mergedExpense.getId()).isEqualTo(expense.getId()),
                () -> assertThat(mergedExpense.getBudgetId()).isEqualTo(expense.getBudgetId()),
                () -> assertThat(mergedExpense.getComment()).isEqualTo(expense.getComment()),
                () -> assertThat(mergedExpense.getAmount()).isEqualTo(expense.getAmount()),
                () -> assertThat(mergedExpense.getExpenseDate()).isEqualTo(expense.getExpenseDate())
        );
    }
}