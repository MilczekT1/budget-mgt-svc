package pl.konradboniecki.budget.budgetmanagement.budget.service;

public class JarCreationException extends RuntimeException {

    public JarCreationException(String message) {
        super(message);
        this.printStackTrace();
    }
}