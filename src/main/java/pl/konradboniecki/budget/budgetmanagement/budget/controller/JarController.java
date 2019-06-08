package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Jar;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/budgets")
public class JarController {

    private JarService jarService;

    @Autowired
    public JarController(JarService jarService) {
        this.jarService = jarService;
    }

    @GetMapping("/{budgetId}/jars/{jarId}")
    public ResponseEntity<Jar> findJar(@PathVariable("budgetId") Long budgetId,
                                       @PathVariable("jarId") Long jarId){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(jarService.findByIdAndBudgetIdOrThrow(jarId, budgetId));
    }

    @GetMapping("/{budgetId}/jars")
    public ResponseEntity<List<Jar>> findJars(@PathVariable("budgetId") Long budgetId){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(jarService.findAllJarsByBudgetId(budgetId));
    }

    @PostMapping("/{budgetId}/jars")
    public ResponseEntity<Jar> saveJar(@RequestBody Jar jarToSave,
                                       @PathVariable("budgetId") Long budgetId){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(jarService.saveJar(jarToSave, budgetId));
    }

    @PutMapping("/{budgetId}/jars/{id}")
    public ResponseEntity<Jar> updateJar(@PathVariable("id") Long jarId,
                                         @PathVariable("budgetId") Long budgetId,
                                         @RequestBody Jar updatedJar){
        return ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(jarService.updateJar(jarId, budgetId, updatedJar));
    }

    @DeleteMapping("/{budgetId}/jars/{id}")
    public ResponseEntity<Void> deleteJar(@PathVariable("id") Long id,
                                          @PathVariable("budgetId") Long budgetId){
        jarService.removeJarFromBudgetOrThrow(id, budgetId);
        return ResponseEntity
                .noContent()
                .build();
    }
}