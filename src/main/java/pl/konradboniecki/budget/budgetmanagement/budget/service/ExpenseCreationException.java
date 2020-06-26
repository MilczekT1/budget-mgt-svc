package pl.konradboniecki.budget.budgetmanagement.budget.service;

import pl.konradboniecki.chassis.exceptions.ResourceCreationException;

public class ExpenseCreationException extends ResourceCreationException {

    public ExpenseCreationException(String message) {
        super(message);
        this.printStackTrace();
    }
}
