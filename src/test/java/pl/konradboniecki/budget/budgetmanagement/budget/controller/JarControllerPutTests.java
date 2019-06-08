package pl.konradboniecki.budget.budgetmanagement.budget.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.konradboniecki.budget.budgetmanagement.BudgetManagementApplication;
import pl.konradboniecki.budget.budgetmanagement.budget.model.Jar;
import pl.konradboniecki.budget.budgetmanagement.budget.service.JarRepository;

import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = BudgetManagementApplication.class,
        webEnvironment = WebEnvironment.MOCK,
        properties = "spring.cloud.config.enabled=false"
)
@AutoConfigureMockMvc
public class JarControllerPutTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JarRepository jarRepository;

    @Test
    public void when_jar_is_updated_then_response_is_ok() throws Exception {
        // Given:
        Long jarId = 1L;
        Jar originJar = new Jar()
                .setId(jarId)
                .setJarName("name")
                .setBudgetId(2L)
                .setCapacity(4L)
                .setCurrentAmount(3L);
        Jar jarInRequestBody = new Jar()
                .setId(jarId)
                .setJarName("modifiedName")
                .setBudgetId(1L)
                .setCapacity(5L)
                .setCurrentAmount(4L);

        // When:
        Jar mergedJar = originJar.mergeWith(jarInRequestBody);
        when(jarRepository.findByIdAndBudgetId(jarId, 1L)).thenReturn(Optional.of(originJar));
        when(jarRepository.save(any(Jar.class))).thenReturn(mergedJar);

        // Then:
        mockMvc.perform(put("/api/budgets/1/jars/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(jarInRequestBody)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString()));
    }

    @Test
    public void when_jar_is_not_found_during_update_then_response_is_ok() throws Exception {
        // Given:
        Long jarId = 1L;
        Jar originJar = new Jar()
                .setId(jarId)
                .setJarName("name")
                .setBudgetId(2L)
                .setCapacity(4L)
                .setCurrentAmount(3L);
        Jar jarInRequestBody = new Jar()
                .setId(jarId)
                .setJarName("modifiedName")
                .setBudgetId(1L)
                .setCapacity(5L)
                .setCurrentAmount(4L);
        // When:
        Jar mergedJar = originJar.mergeWith(jarInRequestBody);
        when(jarRepository.findByIdAndBudgetId(jarId, 1L)).thenReturn(Optional.empty());
        // Then:
        mockMvc.perform(put("/api/budgets/1/jars/1")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(jarInRequestBody)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_UTF8.toString()));
    }
}
