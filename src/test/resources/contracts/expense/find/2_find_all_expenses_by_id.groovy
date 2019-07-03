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
				[
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
				]
			'''
		}
	}
]