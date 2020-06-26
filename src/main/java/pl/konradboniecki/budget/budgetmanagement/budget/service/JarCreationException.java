package pl.konradboniecki.budget.budgetmanagement.budget.service;

import pl.konradboniecki.chassis.exceptions.ResourceCreationException;

public class JarCreationException extends ResourceCreationException {

    public JarCreationException(String message) {
        super(message);
        this.printStackTrace();
    }
}