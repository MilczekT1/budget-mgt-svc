package contracts.expense.find

import org.springframework.cloud.contract.spec.Contract

[
	Contract.make {
		request {
			method GET()
			priority(1)
			url(regex("/api/budgets/1/expenses"))
			headers {
				accept applicationJson()
			}
		}
		response {
			status OK()
			headers {
				contentType applicationJson()
			}
			body '''\
				{
				  "items": [  
				    {
				      "id": 1,
				      "budgetId": 1,
				      "amount": 3,
				      "comment": "test_comments_1",
				      "expenseDate": "2019-06-16T10:22:54.246625Z"
				    },
				    {
				      "id": 2,
				      "budgetId": 1,
				      "amount": 4,
				      "comment": "test_comments_2",
				      "expenseDate": "2019-06-16T10:28:23.053553Z"
				    }
				  ],
				  "_meta": {
				    "page": 0,
				    "pageSize": 100,
				    "totalPages": 1,
				    "elements": 2,
				    "totalElements": 2
				  }
				}
			'''
		}
	},
	Contract.make {
		request {
			method GET()
			priority(1)
			url(regex("/api/budgets/1/expenses")) {
				queryParameters {
					parameter("limit", "1")
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
			body '''\
				{
				  "items": [  
				    {
				      "id": 1,
				      "budgetId": 1,
				      "amount": 3,
				      "comment": "test_comments_1",
				      "expenseDate": "2019-06-16T10:22:54.246625Z"
				    }
				  ],
				  "_meta": {
				    "page": 0,
				    "pageSize": 1,
				    "totalPages": 2,
				    "elements": 1,
				    "totalElements": 2
				  }
				}
			'''
		}
	},
	Contract.make {
		request {
			method GET()
			priority(1)
			url(regex("/api/budgets/1/expenses")) {
				queryParameters {
					parameter("page", "1")
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
			body '''\
				{
				  "items": [  
				    {
				      "id": 2,
				      "budgetId": 1,
				      "amount": 4,
				      "comment": "test_comments_2",
				      "expenseDate": "2019-06-16T10:28:23.053553Z"
				    }
				  ],
				  "_meta": {
				    "page": 1,
				    "pageSize": 100,
				    "totalPages": 2,
				    "elements": 1,
				    "totalElements": 101
				  }
				}
			'''
		}
	}
]
