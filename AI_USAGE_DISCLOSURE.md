# AI Usage Disclosure

## AI Tool Used

I used ChatGPT as an AI coding and learning assistant while developing this assignment.

## How I Used AI

I used ChatGPT to:

- Understand the provided Spring Boot starter project.
- Understand the controller, service, repository, and entity layers.
- Get guidance while implementing the four required transaction operations.
- Review validation and error-handling approaches.
- Develop and improve automated test cases.
- Troubleshoot PowerShell commands and API testing issues.
- Review and prepare project documentation.

## Significant AI Assistance

ChatGPT suggested a layered design using:

- Entity
- Repository
- Service
- Controller
- Exception handling

It also suggested test cases for successful creation, invalid input, duplicate Transaction IDs, missing transactions, blank Transaction IDs, blank Customer IDs, and invalid status updates.

## Changes, Corrections, and Verification

I reviewed the suggested code and implemented it in the provided starter project.

I tested the application locally and made corrections when commands or API requests did not work as expected.

For example, an initial API test using curl produced malformed JSON because of command-line escaping. I corrected the request format and successfully tested the API using PowerShell.

## Final Verification

I verified the final implementation by running:

    .\mvnw.cmd clean test

Final test result:

    Tests run: 8
    Failures: 0
    Errors: 0
    Skipped: 0

    BUILD SUCCESS

I also manually tested:

- Create transaction
- Get transaction
- Update transaction status
- Get customer transactions
- Duplicate transaction handling
- Non-existent transaction handling

The final application and automated test suite were verified locally before submission