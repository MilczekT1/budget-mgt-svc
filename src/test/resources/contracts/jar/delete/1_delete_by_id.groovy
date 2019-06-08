package contracts.jar.delete

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method DELETE()
			priority(1)
			url(regex("/api/budgets/1/jars/1"))
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
			url("/api/budgets/1/jars/2") {
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
				"message": "Jar with id: 2 not found in budget with id: 1"
			)
		}
	}
]
