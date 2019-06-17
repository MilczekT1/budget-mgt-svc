package contracts.jar.delete

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method DELETE()
			priority(1)
			url(regex("/api/budgets/1/expenses/1"))
			headers {
				accept applicationJson()
			}
		}
		response {
			status NO_CONTENT()
		}
	},
	Contract.make {
		request {
			method DELETE()
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
