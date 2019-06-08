package pl.konradboniecki.budget.budgetmanagement.budget.service;


public class JarNotFoundException extends RuntimeException {

    public JarNotFoundException(String message) {
        super(message);
        this.printStackTrace();
    }
}
