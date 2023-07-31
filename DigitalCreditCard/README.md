# Digital Credit Card

This is a mock credit card system, where users are able to input a credit score to be accepted a credit limit, and card number. They can then use the given credit card number to send charges.

## Technologies
- Java
- Vert.x
- Maven
- MongoDB

## Competencies

### JF 1.1

- Can explain all stages of the software development life cycle (what each stage contains, including the inputs and outputs).
- In the development of the Digital Credit Card backend system, I demonstrated a clear understanding of the entire software development life cycle and its stages.
- During the planning stage, I gathered requirements and defined the scope of the credit card system.
- In the design phase, I created software designs and technical specifications based on the requirements.
- During the implementation phase, I coded the backend functionalities using Java and Vert.x, ensuring modularity and maintainability.
- In the testing phase, I conducted various types of tests, to verify the system's correctness and robustness.
- Finally, during the deployment phase, I set up the application on the MongoDB database and made it ready for use.

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



