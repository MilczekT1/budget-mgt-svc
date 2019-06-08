package contracts.jar.create

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method POST()
			url("/api/budgets/1/jars")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				budgetId: 1L,
				jarName: value(producer("name"), consumer(anyNonBlankString())),
				capacity: value(producer(5L), consumer(anyPositiveInt())),
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
				jarName: fromRequest().body("jarName"),
				capacity: fromRequest().body("capacity"),
				currentAmount: 0L,
				status: "IN PROGRESS"
			)
		}
	},
	Contract.make {
		request {
			method POST()
			url("/api/budgets/1/jars")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				budgetId: 3L,
				jarName: value(producer("name"), consumer(anyNonBlankString())),
				capacity: value(producer(5L), consumer(anyPositiveInt())),
			)
		}
		response {
			status BAD_REQUEST()
			headers {
				contentType applicationJson()
			}
			body(
				"timestamp": value(regex("[0-9]{2}-[0-9]{2}-[0-9]{4} [0-2][0-9]:[0-5][0-9]:[0-5][0-9]")),
				"status": 400,
				"statusName": "BAD_REQUEST",
				"message": "Budget id in body and path don't match."
			)
		}
	}
]