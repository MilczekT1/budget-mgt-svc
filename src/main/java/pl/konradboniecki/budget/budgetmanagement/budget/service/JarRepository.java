package pl.konradboniecki.budget.budgetmanagement.budget.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Jar;

import java.util.List;
import java.util.Optional;

@Repository
public interface JarRepository extends CrudRepository<Jar, Long> {
    Optional<Jar> findById(Long id);
    Optional<Jar> findByIdAndBudgetId(Long id, Long budgetId);
    List<Jar> findAllByBudgetId(Long id);
    Jar save(Jar jar);

    @Transactional
    void deleteByIdAndBudgetId(Long id, Long budgetId);
    void deleteById(Long aLong);
}