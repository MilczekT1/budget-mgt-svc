package contracts.budget.find

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method GET()
			url("/api/budgets/1") {
				queryParameters {
					parameter("idType", "invalidValue")
				}
			}
			headers {
				accept applicationJson()
			}
		}
		response {
			status BAD_REQUEST()
		}
	},
	Contract.make {
		request {
			method GET()
			url("/api/budgets/text") {
				queryParameters {
					parameter("idType", "invalidValue")
				}
			}
			headers {
				accept applicationJson()
			}
		}
		response {
			status BAD_REQUEST()
		}
	}
]