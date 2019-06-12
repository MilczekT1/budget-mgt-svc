package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Budget;
import pl.konradboniecki.budget.budgetmanagement.budget.service.BudgetService;

@Slf4j
@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    private BudgetService budgetService;

    @Autowired
    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Budget> findBudget(
            @PathVariable("id") Long id,
            @RequestParam(name = "idType", required = false, defaultValue = "id") String idType) {

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(budgetService.findByOrThrow(id, idType));
    }

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budgetFromBody) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(budgetService.saveBudget(budgetFromBody));
    }
}
