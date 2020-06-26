package pl.konradboniecki.budget.budgetmanagement.budget.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;

import java.util.Optional;

@Repository
public interface ExpenseRepository extends PagingAndSortingRepository<Expense, Long> {

    Page<Expense> findAllByBudgetId(Long id, Pageable pageable);

    Optional<Expense> findByIdAndBudgetId(Long id, Long budgetId);

    Expense save(Expense expense);

    @Transactional
    void deleteByIdAndBudgetId(Long id, Long budgetId);
}
