# Digital Credit Card

This is a mock credit card system, where users are able to input a credit score to be accepted a credit limit, and card number. They can then use the given credit card number to send charges.

## Technologies
- Java
- Vert.x
- Maven
- MongoDB

## Competencies

### JF 1.2
- Can describe the roles and responsibilities within the software development lifecycle (who is responsible for what).
- In the Digital Credit Card project, I was involved in defining the roles and responsibilities of the development team.
- I collaborated with team members to allocate tasks based on individual strengths and expertise.
- I took the responsibility of overseeing the coordination between backend and frontend developers to ensure smooth integration of the credit card system.

### JF 3.6
- Can implement a RESTful API.
- As a part of the Digital Credit Card project, I leveraged the Vert.x framework and Java to develop RESTful APIs for various functionalities.
- I designed and implemented endpoints for credit score input, credit limit assignment, card number generation, and charge transactions.
- The implementation of these APIs enabled seamless communication between the frontend and backend components, allowing users to interact with the credit card system effectively.

### JF 4.7
- Can conduct a range of test types, such as Integration, System, User Acceptance, Non-Functional, Performance, and Security testing.
- Throughout the development of the Digital Credit Card application, I took charge of conducting comprehensive testing to ensure the system's reliability and security.
- I performed integration tests to validate the interactions between different components of the credit card system.
- I conducted user acceptance tests to verify that the credit card functionalities met the requirements and expectations of end-users.
- Additionally, I executed security tests to identify and address potential vulnerabilities in the system, ensuring that users' sensitive credit card information remained secure.

## Building

To launch your tests:
```
./mvnw clean test
```

To package your application:
```
./mvnw clean package
```

To run your application:
```
./mvnw clean compile exec:java
```



