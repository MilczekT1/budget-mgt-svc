package contracts.find

import org.springframework.cloud.contract.spec.Contract

[
    Contract.make {
        request {
            method GET()
            url("/api/budget/1"){
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