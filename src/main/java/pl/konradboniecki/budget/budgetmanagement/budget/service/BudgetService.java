package pl.konradboniecki.budget.budgetmanagement.budget.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Budget;
import pl.konradboniecki.chassis.exceptions.BadRequestException;
import pl.konradboniecki.chassis.exceptions.InternalServerErrorException;

import javax.persistence.PersistenceException;
import java.util.Optional;

@Slf4j
@Service
public class BudgetService {

    private BudgetRepository budgetRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget findByOrThrow(Long id, String idType) {
        switch (idType) {
            case "id":
                return findByIdOrThrow(id);
            case "family":
                return findByFamilyIdOrThrow(id);
            default:
                throw new BadRequestException("Invalid argument idType=" + idType + ", it should be \"id\" or \"family\"");
        }
    }

    private Budget findByIdOrThrow(Long id) {
        Optional<Budget> budget = budgetRepository.findById(id);
        if (budget.isPresent()) {
            return budget.get();
        } else {
            throw new BudgetNotFoundException("Budget with id: " + id + " not found.");
        }
    }

    private Budget findByFamilyIdOrThrow(Long familyId) {
        Optional<Budget> budget = budgetRepository.findByFamilyId(familyId);
        if (budget.isPresent()) {
            return budget.get();
        } else {
            throw new BudgetNotFoundException("Budget not found for family with id: " + familyId);
        }
    }

    public Budget saveBudget(Budget budget) {
        try {
            return budgetRepository.save(budget);
        } catch (PersistenceException e){
            log.error("Failed to save Budget: " + budget);
            throw new InternalServerErrorException("Something bad happened, check your posted data", e);
        }
    }
}
