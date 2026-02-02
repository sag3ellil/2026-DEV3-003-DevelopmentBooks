# Books Store API — Discount Calculator

This project is a Spring Boot REST API for calculating the best price for a basket of books using discount rules.
It follows a **TDD (Test Driven Development)** approach and applies clean separation between **Controller** and **Service** layers.

---

##  Tech Stack

- **Java:** 21
- **Spring Boot:** 3.5.10
- **Maven:** 3.8.1
- **Testing:** JUnit 5, Spring MockMvc, Mockito
- **Lombok:** Used to reduce boilerplate code (getters, constructors, etc.)

---

##  Build & Run

### 1) Build the project
```bash
mvn clean install
```
### 2) Run the application
```bash 
mvn spring-boot:run
```

### OR run the generated JAR:
```bash
java -jar target/books-0.0.1-SNAPSHOT.jar
```

### 3) How to Run Tests
```bash
mvn test
``` 

### API Endpoints
- **POST localhost:8080/api/book/discounts**: Returns the total price for a basket of books.
- 
- ### Example Request Payload
    ```json
    {
      "books": [
        { "bookId": 1, "quantity": 1 },
        { "bookId": 2, "quantity": 1 },
        { "bookId": 3, "quantity": 1 }
      ]
    }
    ```
- ### Successful Response (200 OK)
    ```json
    {
      "price": 135
    }
    ```
- ### Error Response (400 Bad Request)
    ```json
    {
      "status": 400,
      "error": "Bad Request",
      "message": "Basket cannot be empty.",
      "path": "/api/book/discounts
    }
    ```
- ### Error Response (500 Internal Server Error)
  ```json
    {
    "status": 500,
    "error": "Internal Server Error",
    "message": "Unexpected error occurred",
    "path": "/api/book/discounts"
    }
  ```
  ### Basket Validation Rules 
- A request is considered invalid if:
- Basket is empty
- Sum of quantities is 0
- Duplicate books exist (same bookId repeated)
- Any quantity is negative

