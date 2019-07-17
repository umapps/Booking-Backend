[![Build Status](https://dev.azure.com/shridharkalagi/umapps/_apis/build/status/umapps.Booking-Backend?branchName=master)](https://dev.azure.com/shridharkalagi/umapps/_build/latest?definitionId=1&branchName=master)


# Backend Spring Boot Application for the Bookings

**Swagger URL** `http://localhost:8080/swagger-ui.html#`

**Prerequisites for Local Setup** - Java, Maven and Postgresql, redis


How to setup locally?

1. Switch to Postgres CLI from terminal by running `psql postgres`
2. Create Postgres DB - `CREATE DATABASE POSTGRES;` - If already exists proceed to next step
3. Create User - `CREATE ROLE postgres WITH LOGIN PASSWORD 'postgres';` - If already exists proceed to next step
4. Grant permissions - `GRANT ALL PRIVILEGES ON DATABASE postgres TO postgres;`
5. type `exit` to exit the psql CLI
6. Start the server locally - `mvn spring-boot:run` - Starts server at http://localhost:8080


To build the JAR file - Run `mvn clean package` - This will create a JAR in `/target`


To skip the tests - `mvn package -Dmaven.test.skip=true`

To send the SMS/Email from APIs, `application.properties` has to be updated with AWS keys. Reach out to the maintainers.

Slack channel here - `https://um-apps.slack.com`

Invite link - Expires on 30th-July-2019 `https://join.slack.com/t/um-apps/shared_invite/enQtNjY5MDkwODAwMjkxLTJkYmUyMzg3YjIxZTJkNjMzMzllMmYyZWRkZjc0YjgzODdmMTdhNDMyNzg1MTA4ZDA4NmI1YTE5OTM0YWJlMTk`


