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
		//TODO: create contract for conflict
	}
]