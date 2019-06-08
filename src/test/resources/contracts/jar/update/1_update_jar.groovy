package contracts.jar.update

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			priority(1)
			method PUT()
			url("/api/budgets/1/jars/1")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				budgetId: 1L,
				jarName: value(producer("modifiedName"), consumer(anyNonBlankString())),
				capacity: value(producer(5L), consumer(anyPositiveInt())),
				currentAmount: value(producer(4L), consumer(anyPositiveInt())),
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
				jarName: fromRequest().body("jarName"),
				capacity: fromRequest().body("capacity"),
				currentAmount: fromRequest().body("currentAmount"),
				status: "IN PROGRESS"
			)
		}
	},
	Contract.make {
		request {
			priority(2)
			method PUT()
			url("/api/budgets/1/jars/2")
			headers {
				accept applicationJson()
				contentType applicationJson()
			}
			body(
				budgetId: 1L,
				jarName: value(producer("modifiedName"), consumer(anyNonBlankString())),
				capacity: value(producer(5L), consumer(anyPositiveInt())),
				currentAmount: value(producer(4L), consumer(anyPositiveInt())),
			)
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
				"message": "Jar with id: 2 not found in budget with id: 1"
			)
		}
	}
]