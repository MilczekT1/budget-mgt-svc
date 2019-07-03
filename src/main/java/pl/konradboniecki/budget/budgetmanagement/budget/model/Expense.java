package pl.konradboniecki.budget.budgetmanagement.budget.model;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
@Entity
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "thisNameSuxInHibernate5")
    @GenericGenerator(name = "thisNameSuxInHibernate5", strategy = "increment")
    @Column(name = "expense_id")
    private Long id;

    @Column(name = "budget_id")
    private Long budgetId;
    @Column(name = "amount")
    private Long amount;
    @Column(name = "comment")
    private String comment;
    @Column(name = "expense_date")
    private ZonedDateTime expenseDate;

    public Expense() {}

    public Expense mergeWith(@NonNull Expense secondExpense) {
        if (secondExpense.getId() != null)
            setId(secondExpense.getId());
        if (secondExpense.getBudgetId() != null)
            setBudgetId(secondExpense.getBudgetId());
        if (secondExpense.getAmount() != null)
            setAmount(secondExpense.getAmount());
        if (!StringUtils.isEmpty(secondExpense.getComment()))
            setComment(secondExpense.getComment());
        if (secondExpense.getExpenseDate() != null)
            setExpenseDate(secondExpense.getExpenseDate());
        return this;
    }
}
