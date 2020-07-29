# Checkout System

This is an API which simulates a checkout system.

The store owner can :
  - **Create/Remove** a product
  - Update **Price/Description** of a product
  - Create a **discount deal** or a **bundle deal**
  

The client can :
  - **Add/Remove** a product to the basket
  - **Update** the quantity of a product in the basket
  - **Get the total price** of his basket


To simplify the exercise, there is only 1 basket represented by **BasketProduct**

## Technical stack
  - Maven
  - Kotlin
  - Spring Boot
  - H2 Embedded DB
  - Swagger
  
  
## How to run the project ?
  1. Clone the project locally
  2. Run the following commands :
  ```bash
  mvn clean install
  ```
  
  ```bash
  mvn exec:java -P h2
  ```
  
To try the API, go to : http://localhost:8080/swagger-ui.html

You can also access the embedded database here : http://localhost:8080/h2_console
  
