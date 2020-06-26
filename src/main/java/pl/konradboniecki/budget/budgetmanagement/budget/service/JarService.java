package pl.konradboniecki.budget.budgetmanagement.budget.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Jar;

import java.util.List;
import java.util.Optional;

@Service
public class JarService {

    private JarRepository jarRepository;

    @Autowired
    public JarService(JarRepository jarRepository) {
        this.jarRepository = jarRepository;
    }

    Jar findByIdOrThrow(Long id) {
        Optional<Jar> jar = jarRepository.findById(id);
        if (jar.isPresent()) {
            return jar.get();
        } else {
            throw new JarNotFoundException("Jar with id: " + id + " not found.");
        }
    }


    public Jar findByIdAndBudgetIdOrThrow(Long id, Long budgetId) {
        Optional<Jar> jar = jarRepository.findByIdAndBudgetId(id, budgetId);
        if (jar.isPresent()) {
            return jar.get();
        } else {
            throw new JarNotFoundException("Jar with id: " + id + " not found in budget with id: " + budgetId);
        }
    }

    public Jar saveJar(Jar jarFromBody, Long budgetIdFromPath) {
        if (jarFromBody.getBudgetId().equals(budgetIdFromPath)) {
            return jarRepository.save(jarFromBody);
        } else {
            throw new JarCreationException("Budget id in body and path don't match.");
        }
    }

    public Jar updateJar(Long jarId, Long budgetId, Jar newJar) {
        Jar origin = findByIdAndBudgetIdOrThrow(jarId, budgetId);
        return jarRepository.save(origin.mergeWith(newJar));
    }

    public void removeJarFromBudgetOrThrow(Long jarId, Long budgetId) {
        try {
            jarRepository.deleteByIdAndBudgetId(jarId, budgetId);
        } catch (EmptyResultDataAccessException e) {
            throw new JarNotFoundException("Jar with id: " + jarId + " not found in budget with id: " + budgetId);
        }
    }

    public void deleteJarByIdOrThrow(Long id) {
        try {
            jarRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new JarNotFoundException("Jar with id: " + id + " not found.");
        }
    }

    public List<Jar> findAllJarsByBudgetId(Long budgetId) {
        return jarRepository.findAllByBudgetId(budgetId);
    }
}
