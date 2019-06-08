package pl.konradboniecki.budget.budgetmanagement.budget.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@TestInstance(Lifecycle.PER_CLASS)
public class JarTest {

    @Test
    public void set_amount_greater_than_capacity_should_set_status_completed(){
        // Given:
        Jar jar = new Jar().setCapacity(10L);
        // When:
        jar.setCurrentAmount(50L);
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.COMPLETED.getStatus());
    }

    @Test
    public void set_amount_equal_to_capacity_should_set_status_completed(){
        // Given:
        Jar jar = new Jar().setCapacity(10L);
        // When:
        jar.setCurrentAmount(jar.getCapacity());
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.COMPLETED.getStatus());
    }

    @Test
    public void set_amount_lower_than_capacity_should_set_status_IN_PROGRESS(){
        // Given:
        Jar jar = new Jar().setCapacity(10L);
        // When:
        jar.setCurrentAmount(1L);
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.IN_PROGRESS.getStatus());
    }

    @Test
    public void default_status_is_NOT_STARTED(){
        // When:
        Jar jar = new Jar();
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.NOT_STARTED.getStatus());
    }

    @Test
    public void merge_populates_all_properties_if_not_null(){
        // Given:
        Jar jar = new Jar()
                .setId(2L)
                .setBudgetId(2L)
                .setCapacity(2L)
                .setJarName("nameBeforeMerge")
                .setCurrentAmount(1L);
        Jar jarWithSetProperties = new Jar()
                .setId(3L)
                .setBudgetId(3L)
                .setCapacity(3L)
                .setCurrentAmount(3L)
                .setJarName("nameAfterMerge");
        // When:
        Jar mergedJar = jar.mergeWith(jarWithSetProperties);
        // Then:
        Assertions.assertAll(
                () -> assertThat(mergedJar.getId()).isEqualTo(jarWithSetProperties.getId()),
                () -> assertThat(mergedJar.getBudgetId()).isEqualTo(jarWithSetProperties.getBudgetId()),
                () -> assertThat(mergedJar.getCapacity()).isEqualTo(jarWithSetProperties.getCapacity()),
                () -> assertThat(mergedJar.getCurrentAmount()).isEqualTo(jarWithSetProperties.getCurrentAmount()),
                () -> assertThat(mergedJar.getJarName()).isEqualTo(jarWithSetProperties.getJarName()),
                () -> assertThat(mergedJar.getStatus()).isEqualTo(JarStatus.COMPLETED.getStatus())
        );
    }

    @Test
    public void merge_does_not_populate_properties_if_null(){
        // Given:
        Jar jar = new Jar()
                .setId(2L)
                .setBudgetId(2L)
                .setCapacity(2L)
                .setJarName("nameBeforeMerge")
                .setCurrentAmount(1L);
        Jar jarWithSetProperties = new Jar()
                .setId(null)
                .setBudgetId(null)
                .setCurrentAmount(null)
                .setCapacity(null)
                .setJarName(null);
        // When:
        Jar mergedJar = jar.mergeWith(jarWithSetProperties);
        // Then:
        Assertions.assertAll(
                () -> assertThat(mergedJar.getId()).isEqualTo(jar.getId()),
                () -> assertThat(mergedJar.getBudgetId()).isEqualTo(jar.getBudgetId()),
                () -> assertThat(mergedJar.getCapacity()).isEqualTo(jar.getCapacity()),
                () -> assertThat(mergedJar.getCurrentAmount()).isEqualTo(jar.getCurrentAmount()),
                () -> assertThat(mergedJar.getJarName()).isEqualTo(jar.getJarName()),
                () -> assertThat(mergedJar.getStatus()).isEqualTo(JarStatus.IN_PROGRESS.getStatus())
        );
    }

    @Test
    public void merge_throws_NPE_if_car_is_null(){
        // Given:
        Jar firstJar = new Jar();
        // When:
        Throwable throwable = catchThrowable(() -> firstJar.mergeWith(null));
        // Then:
        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void setting_currentAmount_changes_status(){
        // Given:
        Jar firstJar = new Jar()
                .setCapacity(5L)
                .setCurrentAmount(6L);
        assertThat(firstJar.getStatus()).isEqualTo(JarStatus.COMPLETED.getStatus());
        // When:
        firstJar.setCurrentAmount(3L);
        // Then:
        assertThat(firstJar.getStatus()).isEqualTo(JarStatus.IN_PROGRESS.getStatus());
    }
    @Test
    public void setting_capacity_changes_status(){
        // Given:
        Jar firstJar = new Jar()
                .setCurrentAmount(6L)
                .setCapacity(5L);
        assertThat(firstJar.getStatus()).isEqualTo(JarStatus.COMPLETED.getStatus());
        // When:
        firstJar.setCapacity(7L);
        // Then:
        assertThat(firstJar.getStatus()).isEqualTo(JarStatus.IN_PROGRESS.getStatus());
    }

    @Test
    public void setting_capacity_to_negative_changes_status_to_NOT_STARTED(){
        // Given:
        Jar firstJar = new Jar()
                .setCurrentAmount(6L)
                .setCapacity(5L);
        Jar secondJar = new Jar()
                .setCurrentAmount(6L)
                .setCapacity(5L);
        assertThat(firstJar.getStatus()).isEqualTo(JarStatus.COMPLETED.getStatus());
        // When:
        firstJar.setCapacity(0L);
        secondJar.setCapacity(-1L);
        // Then:
        assertThat(firstJar.getStatus()).isEqualTo(JarStatus.NOT_STARTED.getStatus());
        assertThat(secondJar.getStatus()).isEqualTo(JarStatus.NOT_STARTED.getStatus());
    }
    @Test
    public void when_capacity_gt_currentAmount_then_status_is_IN_PROGRESS(){
        // Given:
        Jar jar = new Jar();
        // When:
        jar.setCurrentAmount(2L);
        jar.setCapacity(5L);
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.IN_PROGRESS.getStatus());
    }

    @Test
    public void when_capacity_lt_currentAmount_then_status_is_COMPLETED(){
        // Given:
        Jar jar = new Jar();
        // When:
        jar.setCurrentAmount(6L);
        jar.setCapacity(5L);
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.COMPLETED.getStatus());
    }

    @Test
    public void when_capacity_is_null_then_status_is_NOT_STARTED(){
        // Given:
        Jar jar = new Jar();
        // When:
        jar.setCurrentAmount(6L);
        jar.setCapacity(null);
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.NOT_STARTED.getStatus());
    }

    @Test
    public void when_currentAmount_is_null_then_status_is_NOT_STARTED(){
        // Given:
        Jar jar = new Jar();
        // When:
        jar.setCurrentAmount(null);
        jar.setCapacity(5L);
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.NOT_STARTED.getStatus());
    }

    @Test
    public void when_capacity_is_lt_zero_then_status_is_NOT_STARTED(){
        // Given:
        Jar jar = new Jar();
        // When:
        jar.setCurrentAmount(1L);
        jar.setCapacity(-1L);
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.NOT_STARTED.getStatus());
    }

    @Test
    public void when_capacity_is_equal_to_zero_then_status_is_NOT_STARTED(){
        // Given:
        Jar jar = new Jar();
        // When:
        jar.setCurrentAmount(1L);
        jar.setCapacity(0L);
        // Then:
        assertThat(jar.getStatus()).isEqualTo(JarStatus.NOT_STARTED.getStatus());
    }
}
