[![Build Status](https://travis-ci.org/solomkinmv/glossary.svg?branch=master)](https://travis-ci.org/solomkinmv/glossary)
[![codecov](https://codecov.io/gh/solomkinmv/glossary/branch/master/graph/badge.svg)](https://codecov.io/gh/solomkinmv/glossary)

# glossary
## Set up dev environment
1. Install Lombok plugin for Intellij IDEA
2. Allow annotation processing (Intellij should ask you to allow this)
3. Run `Application` class with active `dev` profile

## Docker
### Build container
`./gradlew web:buildDocker`

### Run container
`docker run -e "SPRING_PROFILES_ACTIVE=dev" -p 8080:8080 -t solomkinmv/glossary`