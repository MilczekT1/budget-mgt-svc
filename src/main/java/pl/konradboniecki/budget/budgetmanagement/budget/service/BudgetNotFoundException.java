package pl.konradboniecki.budget.budgetmanagement.budget.service;

import pl.konradboniecki.chassis.exceptions.ResourceNotFoundException;

public class BudgetNotFoundException extends ResourceNotFoundException {
    public BudgetNotFoundException(String message) {
        super(message);
    }
}
