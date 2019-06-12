package contracts.budget.find

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method GET()
			url("/api/budgets/1")
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
				id: value(producer(1L), consumer(1L)),
				familyId: value(producer(2L), consumer(anyPositiveInt())),
				maxJars: value(producer(6L), consumer(6L))
			)
		}
	},
	Contract.make {
		request {
			method GET()
			url("/api/budgets/1") {
				queryParameters {
					parameter("idType", "id")
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
				id: value(producer(1L), consumer(1L)),
				familyId: value(producer(2L), consumer(anyPositiveInt())),
				maxJars: value(producer(6L), consumer(6L))
			)
		}
	},
	Contract.make {
		request {
			method GET()
			url("/api/budgets/100") {
				queryParameters {
					parameter("idType", "id")
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
				"timestamp": value(regex("[0-9]{2}-[0-9]{2}-[0-9]{4} [0-2][0-9]:[0-5][0-9]:[0-5][0-9]")),
				"status": 404,
				"statusName": "NOT_FOUND",
				"message": "Budget with id: 100 not found."
			)
		}
	}
]