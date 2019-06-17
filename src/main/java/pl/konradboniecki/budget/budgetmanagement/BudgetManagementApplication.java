package pl.konradboniecki.budget.budgetmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"pl.konradboniecki"})
public class BudgetManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(BudgetManagementApplication.class, args);
    }
}
