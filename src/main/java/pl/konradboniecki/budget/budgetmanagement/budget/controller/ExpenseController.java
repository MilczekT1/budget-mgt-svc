package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseService;
import pl.konradboniecki.chassis.tools.PaginatedList;

@RestController
@RequestMapping(value = "/api/budgets/{budgetId}/expenses")
public class ExpenseController {

    private ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<PaginatedList<Expense>> findExpenses(@PathVariable("budgetId") Long budgetId,
                                                               @RequestParam(name = "page", defaultValue = "0") int offset,
                                                               @RequestParam(name = "limit", defaultValue = "100") int limit) {
        Pageable pageable = PageRequest.of(offset, limit);
        Page<Expense> expensePage = expenseService.findAllExpensesByBudgetId(budgetId, pageable);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PaginatedList<>(expensePage));
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense> findExpense(@PathVariable("budgetId") Long budgetId,
                                           @PathVariable("expenseId") Long expenseId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(expenseService.findByIdAndBudgetIdOrThrow(expenseId, budgetId));
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("expenseId") Long id,
                                              @PathVariable("budgetId") Long budgetId) {
        expenseService.removeExpenseFromBudgetOrThrow(id, budgetId);
        return ResponseEntity
                .noContent()
                .build();
    }

    @PostMapping
    public ResponseEntity<Expense> saveExpense(@RequestBody Expense expenseToSave,
                                               @PathVariable("budgetId") Long budgetId) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(expenseService.saveExpense(expenseToSave, budgetId));
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense> updateExpense(@PathVariable("expenseId") Long expenseId,
                                                 @PathVariable("budgetId") Long budgetId,
                                                 @RequestBody Expense updatedExpense) {
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(expenseService.updateExpense(expenseId, budgetId, updatedExpense));
    }
}
