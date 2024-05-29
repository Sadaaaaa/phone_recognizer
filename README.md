# Java app: Phone recognizer
### Test case for NEO Group

## Running the local application
### Prerequisites

- [Docker](https://docs.docker.com/get-docker/) (Or [Docker Compose](https://docs.docker.com/compose/install/) for
  Linux)
- [Maven](https://maven.apache.org/download.cgi)
- [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/download)

###  A) Installation and Running the Application

1) Clone the repository
   ```bash
   git clone https://github.com/Sadaaaaa/phone_recognizer.git
   ```
   
2) Open the project in IntelliJ IDEA
   ```bash
   mvn clean install && mvn spring-boot:run
   ```

### B) Running tests
Run the tests in IntelliJ IDEA
   ```bash
   mvn clean test surefire-report:report
   ```

### C) Running the application using docker-compose
You can run the project via Docker by executing the command in the project's root directory
   ```bash
   docker-compose up
   ```

### D) Deployed application
The deployed application is available at http://kitchentech.site:8088


