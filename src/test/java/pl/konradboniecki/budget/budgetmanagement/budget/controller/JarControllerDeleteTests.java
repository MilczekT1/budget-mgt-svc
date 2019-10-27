package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarRepository;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.MOCK,
        properties = "spring.cloud.config.enabled=false"
)
@AutoConfigureMockMvc
public class JarControllerDeleteTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JarRepository jarRepository;

    // DELETE /api/budgets/{budgetId}/jars/{id}

    @Test
    public void when_jar_found_during_deletion_then_response_is_correct() throws Exception {
        // Given:
        doNothing().when(jarRepository).deleteByIdAndBudgetId(1L, 1L);

        // Then:
        mockMvc.perform(delete("/api/budgets/1/jars/1"))
                .andExpect(status().isNoContent());

    }

    @Test
    public void when_jar_not_found_during_deletion_then_response_is_correct() throws Exception {
        // Given:
        doThrow(EmptyResultDataAccessException.class)
                .when(jarRepository).deleteByIdAndBudgetId(1L, 1L);

        // Then:
        mockMvc.perform(delete("/api/budgets/1/jars/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("Jar with id: 1 not found in budget with id: 1")));
    }
}
