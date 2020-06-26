package pl.konradboniecki.budget.budgetmanagement.budget.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.NONE,
        properties = "spring.cloud.config.enabled=false"
)
public class ExpenseServiceTests {

    @MockBean
    private ExpenseRepository expenseRepository;
    @Autowired
    private ExpenseService expenseService;

    @Test
    public void given_findBy_idAndBudgetId_when_expense_found_then_returned() {
        // Given:
        Expense mockedExpense = new Expense()
                .setAmount(5L)
                .setBudgetId(1L)
                .setId(1L)
                .setComment("testComment");
        when(expenseRepository.findByIdAndBudgetId(1L, 1L))
                .thenReturn(Optional.of(mockedExpense));
        // When:
        Expense expense = expenseService.findByIdAndBudgetIdOrThrow(1L, 1L);
        // Then:
        assertThat(expense).isEqualToComparingFieldByField(mockedExpense);
    }

    @Test
    public void given_findBy_idAndBudgetId_when_expense_not_found_then_thrown() {
        // Given:
        when(expenseRepository.findByIdAndBudgetId(1L, 1L))
                .thenReturn(Optional.empty());
        // When:
        Throwable throwable = catchThrowable(
                () -> expenseService.findByIdAndBudgetIdOrThrow(1L, 1L));
        // Then:
        assertThat(throwable).isInstanceOf(ExpenseNotFoundException.class);
    }

    @Test
    public void given_deleteBy_idAndBudgetId_when_expense_found_then_do_nothing() {
        // Given:
        doNothing().when(expenseRepository).deleteByIdAndBudgetId(1L, 1L);
        // When:
        Throwable throwable = catchThrowable(
                () -> expenseService.removeExpenseFromBudgetOrThrow(1L, 1L));
        // Then:
        assertThat(throwable).isNull();
    }

    @Test
    public void given_deleteBy_idAndBudgetId_when_expense_not_found_then_throw() {
        // Given:
        doThrow(EmptyResultDataAccessException.class)
                .when(expenseRepository)
                .deleteByIdAndBudgetId(1L, 1L);
        // When:
        Throwable throwable = catchThrowable(() -> expenseService.removeExpenseFromBudgetOrThrow(1L, 1L));
        // Then:
        assertThat(throwable).isInstanceOf(ExpenseNotFoundException.class);
    }

    @Test
    public void given_findAll_by_budgetId_when_expenses_not_found_then_return_empty_items() {
        // Given:
        ArrayList<Expense> expenseList = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 100);
        Page<Expense> page = new PageImpl<>(expenseList, pageable, 0);
        when(expenseRepository.findAllByBudgetId(eq(2L), eq(pageable)))
                .thenReturn(page);
        // When:
        Page<Expense> pageWithExpenses = expenseService.findAllExpensesByBudgetId(2L, pageable);
        // Then:
        assertThat(pageWithExpenses).isEmpty();
    }

    @Test
    public void given_findAll_by_budgetId_when_expenses_found_then_return_items() {
        // Given:
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(new Expense());
        expenseList.add(new Expense());
        Pageable pageable = PageRequest.of(0, 100);
        Page<Expense> page = new PageImpl<>(expenseList, pageable, 0);
        when(expenseRepository.findAllByBudgetId(eq(2L), eq(pageable)))
                .thenReturn(page);
        // When:
        Page<Expense> pageWithExpenses = expenseService.findAllExpensesByBudgetId(2L, pageable);
        // Then:
        assertThat(pageWithExpenses.getContent().size()).isEqualTo(2L);
    }

    @Test
    public void given_the_same_budgetId_in_body_and_path_when_save_then_return_expense() {
        // Given:
        Long budgetIdFromExpense = 2L;
        Long budgetIdFromPath = 2L;
        Expense expenseFromBody = new Expense()
                .setBudgetId(budgetIdFromExpense);
        when(expenseRepository.save(any(Expense.class))).thenReturn(new Expense());
        // When:
        Object jarObj = expenseService.saveExpense(expenseFromBody, budgetIdFromPath);
        // Then:
        assertThat(jarObj).isInstanceOf(Expense.class);
    }

    @Test
    public void given_different_budgetId_in_body_and_path_when_save_then_throw() {
        // Given:
        Long budgetIdFromExpense = 1L;
        Long budgetIdFromPath = 2L;
        Expense expenseFromBody = new Expense()
                .setBudgetId(budgetIdFromExpense);
        // When:
        Throwable throwable = catchThrowable(() -> expenseService.saveExpense(expenseFromBody, budgetIdFromPath));
        // Then:
        assertThat(throwable).isInstanceOf(ExpenseCreationException.class);
    }

    @Test
    public void given_different_budgetId_in_body_and_path_when_update_then_throw() {
        // Given:
        Long budgetIdFromExpense = 2L;
        Long budgetIdFromPath = 1L;
        Expense expenseFromBody = new Expense()
                .setId(1L)
                .setBudgetId(budgetIdFromExpense);
        // When:
        Throwable throwable = catchThrowable(
                () -> expenseService.updateExpense(expenseFromBody.getId(), budgetIdFromPath, expenseFromBody));
        // Then:
        assertThat(throwable).isInstanceOf(ExpenseNotFoundException.class);
    }


    @Test
    public void given_the_same_budgetId_in_body_and_path_when_update_then_return_expense() {
        // Given:
        Long budgetIdFromExpense = 2L;
        Long budgetIdFromPath = 2L;
        Expense expenseFromBody = new Expense()
                .setId(1L)
                .setBudgetId(budgetIdFromExpense);
        when(expenseRepository.findByIdAndBudgetId(1L, 2L))
                .thenReturn(Optional.of(expenseFromBody));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expenseFromBody);
        // When:
        Object jarObj = expenseService.updateExpense(expenseFromBody.getId(), budgetIdFromPath, expenseFromBody);
        // Then:
        assertThat(jarObj).isInstanceOf(Expense.class);
    }
}
