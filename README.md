[![Build Status](https://travis-ci.org/solomkinmv/glossary.svg?branch=master)](https://travis-ci.org/solomkinmv/glossary)
[![codecov](https://codecov.io/gh/solomkinmv/glossary/branch/master/graph/badge.svg)](https://codecov.io/gh/solomkinmv/glossary)

# glossary
## Set up dev environment
1. Install Lombok plugin for Intellij IDEA
2. Allow annotation processing (Intellij should ask you to allow this)
3. Configure additional javac parameter `-params`
4. Run `Application` class with active `dev` profile

# Microservices Flow

### Run docker-compose
`docker-compose up`

## Monitoring

### Zipkin
Open request tracing by visiting `http://localhost:9411/zipkin/`

### Hystrix dashboard

Visit dashboard: `http://localhost:9001/hystrix`.

Use turbine stream to open dashboard: `http://localhost:9001/turbine.stream`.`

# Monolith Legacy Flow
## Docker
### Build container
`./gradlew web:buildDocker`

### Run container
`docker run -e "SPRING_PROFILES_ACTIVE=dev" -p 8080:8080 -t solomkinmv/glossary`