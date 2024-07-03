# E-commerce Order Processing Service

This project implements a web service designed to handle high volume traffic of order details from different e-commerce platforms. The service ensures performance and reliability by utilizing Kafka for message queuing and Spring Boot for handling incoming requests and processing them efficiently.

## Features

- Handles high volume traffic without becoming unresponsive (probably).
- Logs each received request in the database.
- Mocks e-mail sending service (no actual e-mails are sent).
- Provides controllable performance for both receiving and sending data functions.

## Technologies Used

- Java 17
- Spring Boot
- Kafka
- Docker
- H2 DB
- and some libs (like Guava, Lombok)

## Getting Started

### Prerequisites

- Docker
- Docker Compose

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/Xayanix/OrderServiceAPI.git
    cd OrderServiceAPI
    ```

2. Compile app using Maven (requires at least 3.6.3+ version):
    ```bash
    mvn clean package
    ```

3. Build and run the application using Docker Compose:
    ```bash
    docker-compose up --build
    ```

### Docker Hub

The Docker image for this application is available on Docker Hub:
[Docker Hub Image](https://hub.docker.com/r/xayanix/dpdgroupproject-spring-app)

### Architectural Diagram

You can view the architectural diagram of the proposed solution [here](https://boardmix.com/app/share/CAE.CJyrKiABKhDXPAUnmJRnTtpvafM4rTM2MAZAAQ/YflX56?elementNodeGuid=1:8).

## Usage

Once the application is running, it will listen on port 8080 for incoming order details and process them accordingly.


## Testing

Tests can be run using:
```bash
mvn test
```

### Example CURLs
(can be imported into Postman)

Post
```
curl --location 'http://localhost:8080/orders' \
--header 'Content-Type: application/json' \
--data-raw '{
    "shipmentNumber": "12345",
    "receiverEmail": "receiver@example.com",
    "receiverCountryCode": "US",
    "senderCountryCode": "UK",
    "statusCode": 50
}'
```

Patch
```
curl --location --request PATCH 'http://localhost:8080/orders/1' \
--header 'Content-Type: application/json' \
--data-raw '{
    "shipmentNumber": "12345",
    "receiverCountryCode": "DE",
    "statusCode": 20
}'
```
