# Gas & Water Usage Monitoring Application

## Introduction
This application is used to submit current measurements of a given user and to receive history of previous measurements
for given user.

## Features
- Submitting current measurements of a given user (gas, hot water, cold water)
- Getting history of previous measurements for given user

### Prerequisites
- JDK 8

### Installing
Install dependencies:

```mvn -N wrapper:wrapper```

```./mvnw clean install```

Run the application:

```./mvnw spring-boot:run```

## Usage
ui: http://localhost:8081/swagger-ui/

json: http://localhost:8081/v3/api-docs
