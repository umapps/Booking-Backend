[![Build Status](https://dev.azure.com/shridharkalagi/umapps/_apis/build/status/umapps.Booking-Backend?branchName=master)](https://dev.azure.com/shridharkalagi/umapps/_build/latest?definitionId=1&branchName=master)


# Backend Spring Boot Application for the Bookings

**Swagger URL** `http://localhost:8080/swagger-ui.html#`

**Prerequisites for Local Setup** - Java, Maven, Postgresql and redis


How to setup locally?

1. Switch to Postgres CLI from terminal by running `psql postgres`
2. Create Postgres DB - `CREATE DATABASE POSTGRES;` - If already exists proceed to next step
3. Create User - `CREATE ROLE postgres WITH LOGIN PASSWORD 'postgres';` - If already exists proceed to next step
4. Grant permissions - `GRANT ALL PRIVILEGES ON DATABASE postgres TO postgres;`
5. type `exit` to exit the psql CLI
6. Start the server locally without AWS services (Email & SMS)  - `mvn spring-boot:run`
7. Start the server locally with AWS services (Email & SMS) - `mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Daws.accessKeyId=********** -Daws.secretKey=*********"` (Reach out to administrators for AWS keys)
 - Starts server at http://localhost:8080

To setup in IDE, below two VM options / Arguments should be added to use AWS services (Rest all features will work fine)
-Daws.accessKeyId=********** -Daws.secretKey=***********

To build the JAR file - Run `mvn clean package` - This will create a JAR in `/target`


To skip the tests - `mvn package -Dmaven.test.skip=true`

Slack channel here - `https://um-apps.slack.com`

Invite link - Expires on 30th-July-2019 `https://join.slack.com/t/um-apps/shared_invite/enQtNjY5MDkwODAwMjkxLTJkYmUyMzg3YjIxZTJkNjMzMzllMmYyZWRkZjc0YjgzODdmMTdhNDMyNzg1MTA4ZDA4NmI1YTE5OTM0YWJlMTk`


