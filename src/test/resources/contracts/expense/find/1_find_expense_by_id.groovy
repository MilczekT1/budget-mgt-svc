package contracts.expense.find

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method GET()
			priority(1)
			url(regex("/api/budgets/1/expenses/1"))
			headers {
				accept applicationJson()
			}
		}
		response {
			status OK()
			headers {
				contentType applicationJson()
			}
			body(
				id: value(fromRequest().path(4)),
				budgetId: value(fromRequest().path(2)),
				amount: value(anyPositiveInt()),
				comment: value(anyNonEmptyString()),
				expenseDate: value(regex("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,6}Z"))
			)
		}
	},
	Contract.make {
		request {
			method GET()
			priority(2)
			url("/api/budgets/1/expenses/2") {
			}
			headers {
				accept applicationJson()
			}
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