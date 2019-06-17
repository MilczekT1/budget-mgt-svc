package contracts.expense.update

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			priority(1)
			method PUT()
			url("/api/budgets/1/expenses/1")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				id: 1L,
				budgetId: 1L,
				labelId: 1L,
				comment: value(producer("edited_comment"), consumer(anyNonBlankString()))
			)
		}
		response {
			status OK()
			headers {
				contentType applicationJson()
			}
			body(
				id: 1L,
				budgetId: fromRequest().body("budgetId"),
				labelId: fromRequest().body("labelId"),
				amount: anyPositiveInt(),
				comment: fromRequest().body("comment"),
				expenseDate: regex("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,6}Z")
			)
		}
	},
	Contract.make {
		request {
			priority(2)
			method PUT()
			url("/api/budgets/1/expenses/2")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				id: 2L,
				budgetId: 1L,
				amount: value(producer(5L), consumer(anyPositiveInt())),
			)
		}
		response {
			status NOT_FOUND()
			headers {
				contentType applicationJson()
			}
			body(
				"timestamp": value(regex("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,6}Z")),
				"status": 404,
				"statusName": "NOT_FOUND",
				"message": "Expense with id: 2 not found in budget with id: 1."
			)
		}
	}
]