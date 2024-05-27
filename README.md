# Java app: Phone recognizer
### Тестовое задание для NEO Group

## Запуск локального приложения
### Необходимые приложения

- [Docker](https://docs.docker.com/get-docker/) (Или [Docker Compose](https://docs.docker.com/compose/install/) для
  Linux)
- [Maven](https://maven.apache.org/download.cgi)
- [IntelliJ IDEA](https://www.jetbrains.com/ru-ru/idea/download)

### Установка

1) Склонируйте репозиторий
   ```bash
   git clone https://github.com/Sadaaaaa/phone_recognizer.git
   ```
   
2) Откройте проект в IntelliJ IDEA
   ```bash
   mvn clean install && mvn spring-boot:run
   ```

3) Запустите тесты IntelliJ IDEA
   ```bash
   mvn clean test surefire-report:report
   ```

4) Или запустите проект через Docker, выполнив команду в корневой директории проекта
   ```bash
   docker-compose up
   ```
   
5) Запущенное приложение доступно по адресу http://kitchentech.site:8088


