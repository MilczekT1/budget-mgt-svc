package contracts.find

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        request {
            method GET()
            url("/api/budget/1") {
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
            url("/api/budget/100"){
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
        }
    }
]