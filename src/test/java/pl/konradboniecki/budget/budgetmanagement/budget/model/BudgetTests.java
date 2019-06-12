package pl.konradboniecki.budget.budgetmanagement.budget.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BudgetTests {

    @Test
    public void default_maxJars_is_equal_to_6(){
        // When:
        Budget bgt = new Budget();
        // Then:
        assertThat(bgt.getMaxJars()).isEqualTo(6L);
    }
}
