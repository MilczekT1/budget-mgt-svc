package contracts.budget.save

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method POST()
			url("/api/budgets")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				familyId: value(producer(5L), consumer(anyPositiveInt())),
				maxJars: value(producer(6L), consumer(6L))
			)
		}
		response {
			status CREATED()
			headers {
				contentType applicationJson()
			}
			body(
				id: value(producer(5L), consumer(anyPositiveInt())),
				familyId: value(producer(5L), consumer(fromRequest().body("familyId"))),
				maxJars: value(producer(6L), consumer(fromRequest().body("maxJars")))
			)
		}
	},
	Contract.make {
		request {
			method POST()
			url("/api/budgets")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				familyId: value(producer(8L), consumer(anyPositiveInt())),
				maxJars: value(producer(6L), consumer(6L))
			)
		}
		response {
			status INTERNAL_SERVER_ERROR()
			headers {
				contentType applicationJson()
			}
			body(
				"timestamp": value(regex("[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-2][0-9]:[0-5][0-9]:[0-5][0-9].[0-9]{1,6}Z")),
				"status": 500,
				"statusName": "INTERNAL_SERVER_ERROR",
				"message": value("Something bad happened, check your posted data")
			)
		}
	}
]