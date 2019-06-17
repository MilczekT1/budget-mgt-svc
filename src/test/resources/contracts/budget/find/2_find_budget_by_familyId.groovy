package contracts.budget.find

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method GET()
			url("/api/budgets/1") {
				queryParameters {
					parameter("idType", "family")
				}
			}
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
				id: value(producer(2L), consumer(anyPositiveInt())),
				familyId: value(producer(1L), consumer(1L)),
				maxJars: value(producer(6L), consumer(6L))
			)
		}
	},
	Contract.make {
		request {
			method GET()
			url("/api/budgets/100") {
				queryParameters {
					parameter("idType", "family")
				}
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
				"message": "Budget not found for family with id: 100"
			)
		}
	}
]