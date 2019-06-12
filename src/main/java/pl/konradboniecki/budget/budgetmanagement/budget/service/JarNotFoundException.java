package pl.konradboniecki.budget.budgetmanagement.budget.service;


import pl.konradboniecki.chassis.exceptions.ResourceNotFoundException;

public class JarNotFoundException extends ResourceNotFoundException {

    public JarNotFoundException(String message) {
        super(message);
    }
}
