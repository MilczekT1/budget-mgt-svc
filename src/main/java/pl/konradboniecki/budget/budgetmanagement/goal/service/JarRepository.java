package pl.konradboniecki.budget.budgetmanagement.goal.service;

import org.springframework.data.repository.CrudRepository;
import pl.konradboniecki.budget.budgetmanagement.goal.model.Jar;

//@Repository
public interface JarRepository extends CrudRepository<Jar, Long> {
//    Optional<Jar> findById(Long id);
//    Optional<Jar> findByBudgetId(Long id);
//    List<Jar> findAllByBudgetId(Long id);
//    Jar save(Jar jar);
//    long count();
//    void deleteById(Long aLong);
//
//    @Modifying
//    @Transactional
//    @Query(value="UPDATE jar SET current_amount = ?1 WHERE jar_id = ?2", nativeQuery=true)
//    void setCurrentAmount(Long newAmount, Long jarId);
}