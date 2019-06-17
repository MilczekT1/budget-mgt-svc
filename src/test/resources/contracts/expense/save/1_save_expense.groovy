package contracts.jar.create

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method POST()
			url("/api/budgets/1/expenses")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				budgetId: 1L,
				labelId: 1L,
				amount: value(producer(5L), consumer(anyPositiveInt())),
				comment: value(producer("comment"), consumer(anyNonBlankString()))
			)
		}
		response {
			status CREATED()
			headers {
				contentType applicationJson()
			}
			body(
				id: value(producer(1L), consumer(anyPositiveInt())),
				budgetId: fromRequest().body("budgetId"),
				labelId: fromRequest().body("labelId"),
				amount: fromRequest().body("amount"),
				comment: fromRequest().body("comment"),
				expenseDate: regex("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,6}Z")
			)
		}
	},
	Contract.make {
		request {
			description("Only required properties in body")
			method POST()
			url("/api/budgets/1/expenses")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				budgetId: 1L,
				amount: value(producer(5L), consumer(anyPositiveInt())),
			)
		}
		response {
			status CREATED()
			headers {
				contentType applicationJson()
			}
			body(
				id: value(producer(1L), consumer(anyPositiveInt())),
				budgetId: fromRequest().body("budgetId"),
				labelId: null,
				amount: fromRequest().body("amount"),
				comment: null,
				expenseDate: regex("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,6}Z")
			)
		}
	},
	Contract.make {
		request {
			method POST()
			url("/api/budgets/1/expenses")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				budgetId: 2L,
				amount: anyPositiveInt(),
			)
		}
		response {
			status BAD_REQUEST()
			headers {
				contentType applicationJson()
			}
			body(
				"timestamp": value(regex("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,6}Z")),
				"status": 400,
				"statusName": "BAD_REQUEST",
				"message": "Budget id in body and path don't match."
			)
		}
	}
]