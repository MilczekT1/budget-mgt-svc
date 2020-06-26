package pl.konradboniecki.budget.budgetmanagement.budget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;

import java.util.Optional;

@Service
public class ExpenseService {

    private ExpenseRepository expenseRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public Expense findByIdAndBudgetIdOrThrow(Long id, Long budgetId) {
        Optional<Expense> expense = expenseRepository.findByIdAndBudgetId(id, budgetId);
        if (expense.isPresent()) {
            return expense.get();
        } else {
            throw new ExpenseNotFoundException("Expense with id: " + id + " not found in budget with id: " + budgetId + ".");
        }
    }

    public Page<Expense> findAllExpensesByBudgetId(Long budgetId, Pageable pageable) {
        return expenseRepository.findAllByBudgetId(budgetId, pageable);
    }

    public void removeExpenseFromBudgetOrThrow(Long expenseId, Long budgetId) {
        try {
            expenseRepository.deleteByIdAndBudgetId(expenseId, budgetId);
        } catch (EmptyResultDataAccessException e) {
            throw new ExpenseNotFoundException("Expense with id: " + expenseId + " not found in budget with id: " + budgetId + ".");
        }
    }

    public Expense saveExpense(Expense expenseFromBody, Long budgetIdFromPath) {
        if (expenseFromBody.getBudgetId().equals(budgetIdFromPath)) {
            return expenseRepository.save(expenseFromBody);
        } else {
            throw new ExpenseCreationException("Budget id in body and path don't match.");
        }
    }

    public Expense updateExpense(Long originID, Long budgetId, Expense newExpense) {
        Expense origin = findByIdAndBudgetIdOrThrow(originID, budgetId);
        return expenseRepository.save(origin.mergeWith(newExpense));
    }
}
