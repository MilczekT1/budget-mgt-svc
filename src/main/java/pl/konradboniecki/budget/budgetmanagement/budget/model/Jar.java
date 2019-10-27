package pl.konradboniecki.budget.budgetmanagement.budget.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Entity
public class Jar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator(name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name = "jar_id")
    private Long id;

    @Column(name = "budget_id")
    private Long budgetId;
    @Column(name = "jar_name")
    private String jarName;
    @Column(name = "current_amount")
    private Long currentAmount;
    @Column(name = "capacity")
    private Long capacity;
    @Column(name = "status")
    @Setter(AccessLevel.NONE)
    private String status;

    public Jar() {
        setCapacity(0L);
        setCurrentAmount(0L);
    }

    public Jar mergeWith(@NonNull Jar secondJar) {
        if (secondJar.getId() != null)
            setId(secondJar.getId());
        if (secondJar.getBudgetId() != null)
            setBudgetId(secondJar.getBudgetId());
        if (secondJar.getCapacity() != null)
            setCapacity(secondJar.getCapacity());
        if (secondJar.getCurrentAmount() != null)
            setCurrentAmount(secondJar.getCurrentAmount());
        if (!StringUtils.isEmpty(secondJar.getJarName()))
            setJarName(secondJar.getJarName());
        setStatus();
        return this;
    }

    public Jar setCurrentAmount(Long newAmount) {
        this.currentAmount = newAmount;
        setStatus();
        return this;
    }

    public Jar setCapacity(Long newCapacity) {
        this.capacity = newCapacity;
        setStatus();
        return this;
    }

    private Jar setStatus() {
        if (currentAmount == null || capacity == null || capacity <= 0L) {
            this.status = JarStatus.NOT_STARTED.getStatus();
        } else if (currentAmount < capacity) {
            this.status = JarStatus.IN_PROGRESS.getStatus();
        } else {
            this.status = JarStatus.COMPLETED.getStatus();
        }
        return this;
    }
}
