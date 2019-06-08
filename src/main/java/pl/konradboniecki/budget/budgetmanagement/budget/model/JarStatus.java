package pl.konradboniecki.budget.budgetmanagement.budget.model;

import lombok.Getter;
import lombok.Setter;

public enum JarStatus {
    COMPLETED("COMPLETED"),
    IN_PROGRESS("IN PROGRESS"),
    NOT_STARTED("NOT STARTED");

    JarStatus(String status){
        setStatus(status);
    }

    @Getter
    @Setter
    private String status;
}
