package pl.konradboniecki.budget.budgetmanagement.budget.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Budget;

import java.util.Optional;

@Repository
public interface BudgetRepository extends CrudRepository<Budget, Long> {
    Optional<Budget> findByFamilyId(Long id);
    Optional<Budget> findById(Long id);
    Budget save(Budget budget);
}
