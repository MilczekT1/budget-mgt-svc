package pl.konradboniecki.budget.budgetmanagement.budget.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Budget;
import pl.konradboniecki.budget.budgetmanagement.budget.model.BudgetRepository;

import java.util.Optional;

@Slf4j
@Service
public class BudgetService {

    private BudgetRepository budgetRepository;

    @Autowired
    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public Budget findBy(Long id, String idType){
        switch (idType) {
            case "id":
                return findById(id);
            case "family" :
                return findByFamilyId(id);
            default:
                //TODO: test message
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument idType=" + idType + ", it should be \"id\" or \"family\"");
        }
    }

    private Budget findById(Long id){
        Optional<Budget> budget =  budgetRepository.findById(id);
        if (budget.isPresent()){
            return budget.get();
        } else {
            //TODO: test message
            throw new BudgetNotFoundException("Budget with id: " + id + " not found.");
        }
    }

    private Budget findByFamilyId(Long familyId){
        Optional<Budget> budget =  budgetRepository.findByFamilyId(familyId);
        if (budget.isPresent()){
            return budget.get();
        } else {
            //TODO: test message
            throw new BudgetNotFoundException("Budget not found for family with id: " + familyId);
        }
    }

    public Budget saveBudget(Budget budget){
        return budgetRepository.save(budget);
    }
}
