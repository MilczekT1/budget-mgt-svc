package pl.konradboniecki.budget.budgetmanagement.contractbases;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Budget;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Expense;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Jar;
import pl.konradboniecki.budget.budgetmanagement.budget.service.BudgetRepository;
import pl.konradboniecki.budget.budgetmanagement.budget.service.ExpenseRepository;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarRepository;

import javax.persistence.PersistenceException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static io.restassured.config.RedirectConfig.redirectConfig;
import static io.restassured.config.RestAssuredConfig.config;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.RANDOM_PORT
)
public abstract class MvcClientBase {
    @LocalServerPort
    int port;

    @MockBean
    private JarRepository jarRepository;
    @MockBean
    private BudgetRepository budgetRepository;
    @MockBean
    private ExpenseRepository expenseRepository;

    @Before
    public void setUpMocks() {
        RestAssured.baseURI = "http://localhost:" + this.port;
        RestAssured.config = config().redirect(redirectConfig().followRedirects(false));
        mock_jar_delete();
        mock_jar_save();
        mock_jar_update();
        mock_jar_find();
        mock_jar_find_all();

        mock_budget_find();
        mock_budget_save();

        mock_expense_delete();
        mock_expense_save();
        mock_expense_find_all();
    }

    private void mock_jar_delete() {
        doNothing().when(jarRepository).deleteByIdAndBudgetId(eq(1L), eq(1L));
        doThrow(EmptyResultDataAccessException.class)
                .when(jarRepository).deleteByIdAndBudgetId(eq(5L), eq(1L));
    }

    private void mock_jar_save() {
        Jar jarToSave = new Jar()
                .setJarName("name")
                .setBudgetId(1L)
                .setCapacity(5L);
        Jar savedJar = new Jar()
                .setJarName("name")
                .setBudgetId(1L)
                .setCapacity(5L)
                .setCurrentAmount(0L)
                .setId(1L);
        when(jarRepository.save(jarToSave)).thenReturn(savedJar);
    }

    private void mock_jar_update() {
        Jar jarBeforeModification = new Jar()
                .setId(4L)
                .setBudgetId(4L)
                .setJarName("notModifiedName")
                .setCapacity(5L)
                .setCurrentAmount(4L);
        Jar jarAfterModification = new Jar()
                .setId(4L)
                .setBudgetId(4L)
                .setJarName("modifiedName")
                .setCapacity(5L)
                .setCurrentAmount(4L);
        when(jarRepository.findByIdAndBudgetId(eq(4L), eq(4L)))
                .thenReturn(Optional.of(jarBeforeModification));
        when(jarRepository.findByIdAndBudgetId(eq(5L), eq(1L)))
                .thenReturn(Optional.empty());
        when(jarRepository.save(jarAfterModification)).thenReturn(jarAfterModification);
    }

    private void mock_jar_find() {
        Jar jarToFind = new Jar()
                .setId(1L)
                .setBudgetId(1L)
                .setCapacity(3L)
                .setCurrentAmount(2L)
                .setJarName("foundJar");
        when(jarRepository.findByIdAndBudgetId(eq(1L), eq(1L)))
                .thenReturn(Optional.of(jarToFind));
        when(jarRepository.findByIdAndBudgetId(eq(5L), any()))
                .thenReturn(Optional.empty());
    }

    private void mock_jar_find_all() {
        Jar firstJar = new Jar()
                .setId(1L)
                .setBudgetId(1L)
                .setJarName("name1")
                .setCurrentAmount(0L)
                .setCapacity(3L);
        Jar secondJar = new Jar()
                .setId(2L)
                .setBudgetId(1L)
                .setJarName("name2")
                .setCurrentAmount(0L)
                .setCapacity(3L);
        ArrayList<Jar> list = new ArrayList<>(2);
        list.add(firstJar);
        list.add(secondJar);
        when(jarRepository.findAllByBudgetId(eq(1L))).thenReturn(list);
        when(jarRepository.findAllByBudgetId(eq(2L))).thenReturn(Collections.emptyList());
    }

    private void mock_budget_find() {
        Budget foundBudget = new Budget()
                .setId(2L)
                .setFamilyId(1L)
                .setMaxJars(6L);
        when(budgetRepository.findByFamilyId(eq(1L)))
                .thenReturn(Optional.of(foundBudget));
        when(budgetRepository.findByFamilyId(eq(100L)))
                .thenReturn(Optional.empty());
    }

    private void mock_budget_save() {
        Budget failureBody = new Budget()
                .setFamilyId(8L)
                .setMaxJars(8L);
        doThrow(PersistenceException.class).when(budgetRepository).save(eq(failureBody));
        Budget budgetToSave = new Budget()
                .setFamilyId(6L)
                .setMaxJars(6L);
        Budget savedBudget = new Budget()
                .setId(5L)
                .setFamilyId(6L)
                .setMaxJars(6L);
        when(budgetRepository.save(budgetToSave)).thenReturn(savedBudget);
    }

    private void mock_expense_delete() {
        doNothing().when(expenseRepository)
                .deleteByIdAndBudgetId(eq(1L), eq(1L));
        doThrow(EmptyResultDataAccessException.class)
                .when(expenseRepository).deleteByIdAndBudgetId(eq(2L), eq(1L));
    }

    private void mock_expense_save() {
        Expense expenseToSave = new Expense()
                .setBudgetId(1L)
                .setAmount(5L)
                .setComment("comment");
        Expense savedExpense = new Expense()
                .setBudgetId(1L)
                .setAmount(5L)
                .setComment("comment")
                .setId(1L)
                .setCreated(Instant.now());
        when(expenseRepository.save(eq(expenseToSave))).thenReturn(savedExpense);
    }

    private void mock_expense_find_all() {
        Expense firstExpense = new Expense()
                .setId(1L)
                .setBudgetId(1L)
                .setAmount(3L)
                .setComment("test_comments_1")
                .setCreated(Instant.parse("2019-06-16T10:22:54.246625Z"));
        Expense secondExpense = new Expense()
                .setId(2L)
                .setBudgetId(1L)
                .setAmount(4L)
                .setComment("test_comments_2")
                .setCreated(Instant.parse("2019-06-16T10:28:23.053553Z"));
        ArrayList<Expense> expenseList = new ArrayList<>();
        expenseList.add(firstExpense);
        expenseList.add(secondExpense);
        Pageable pageable = PageRequest.of(0, 100);
        Page<Expense> page = new PageImpl<>(expenseList, pageable, 2);
        when(expenseRepository.findAllByBudgetId(eq(1L), eq(pageable)))
                .thenReturn(page);
        Page<Expense> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(expenseRepository.findAllByBudgetId(10L, pageable)).thenReturn(emptyPage);
    }
}
