package pl.konradboniecki.budget.budgetmanagement.budget.service;

import pl.konradboniecki.chassis.exceptions.ResourceNotFoundException;

public class ExpenseNotFoundException extends ResourceNotFoundException {

    public ExpenseNotFoundException(String message) {
        super(message);
    }
}
