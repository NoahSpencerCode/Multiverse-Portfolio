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

### JF 4.2
- Acts with integrity with respect to ethical, legal, and regulatory ensuring the protection of personal data, safety, and security.
- During the development of the Digital Credit Card backend system, I prioritized data security and compliance with relevant legal and regulatory standards.
- I ensured that sensitive user data, such as credit card numbers and personal information, were encrypted and stored securely in the MongoDB database.

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



