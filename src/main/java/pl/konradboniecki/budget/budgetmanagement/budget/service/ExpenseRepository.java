package pl.konradboniecki.budget.budgetmanagement.budget.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpenseRepository extends CrudRepository<Expense, Long> {

    List<Expense> findAllByBudgetId(Long id);

    Optional<Expense> findByIdAndBudgetId(Long id, Long budgetId);

    Expense save(Expense expense);

    @Transactional
    void deleteByIdAndBudgetId(Long id, Long budgetId);
}