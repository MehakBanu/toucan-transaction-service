# AI Usage Disclosure

## AI Tool Used

ChatGPT was used as an AI coding assistant during this assignment.

## How AI Was Used

I used ChatGPT for:

- Understanding the assignment requirements.
- Discussing the project structure and implementation approach.
- Getting guidance on Java and Spring Boot concepts.
- Getting suggestions for validation and error handling.
- Getting assistance with automated test cases.
- Troubleshooting compilation and Maven setup issues.
- Reviewing and improving the project documentation.

## AI-Generated Suggestions

ChatGPT provided guidance, code examples and suggestions for parts of the:

- Entity
- DTOs
- Repository
- Service layer
- REST controller
- Exception handling
- Automated tests
- README and documentation

I reviewed the suggestions and integrated the relevant parts into the
provided starter project.

## Corrections and Decisions

During development, I identified and corrected a package/location issue after
moving `TransactionService.java` into the `service` package.

The Maven setup initially had a JAVA_HOME configuration issue. I corrected
the configuration and verified that the provided Maven wrapper worked.

The assignment materials I received did not contain a specific candidate
variant. Therefore, I did not add unsupported currency, maximum-amount or
transaction-type restrictions. I defined reasonable validation rules based
on the requirements and documented them in the README.

For transaction status, I chose to allow a `PENDING` transaction to move to
`COMPLETED`, `FAILED` or `CANCELLED`, while treating these as final states.

## Verification

I verified the final application using the provided Maven wrapper:

`.\mvnw.cmd clean test`

The final result was:

`Tests run: 7, Failures: 0, Errors: 0, Skipped: 0`

`BUILD SUCCESS`

The tests cover successful creation, validation failure, duplicate
transaction IDs, non-existent transactions, status updates and customer
transaction retrieval.