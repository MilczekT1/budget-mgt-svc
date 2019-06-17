package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/budgets/{budgetId}/expenses")
public class ExpenseController {

    private ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<List<Expense>> findJars(@PathVariable("budgetId") Long budgetId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(expenseService.findAllExpensesByBudgetId(budgetId));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> findExpense(@PathVariable("budgetId") Long budgetId,
                                           @PathVariable("expenseId") Long expenseId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(expenseService.findByIdAndBudgetIdOrThrow(expenseId, budgetId));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteJar(@PathVariable("expenseId") Long id,
                                          @PathVariable("budgetId") Long budgetId) {
        expenseService.removeExpenseFromBudgetOrThrow(id, budgetId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping
    public ResponseEntity<Expense> saveJar(@RequestBody Expense expenseToSave,
                                       @PathVariable("budgetId") Long budgetId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(expenseService.saveExpense(expenseToSave, budgetId));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense> updateJar(@PathVariable("expenseId") Long expenseId,
                                         @PathVariable("budgetId") Long budgetId,
                                         @RequestBody Expense updatedExpense) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(expenseService.updateExpense(expenseId, budgetId, updatedExpense));
    }
}
