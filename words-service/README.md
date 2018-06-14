# Words Service

Words Service handles managing (CRUD) of words and sets of words. 
Creates different types of practice exercises and handles results.

Requires config server to be up and running `http://config:8888`.
Requires mariaDB database connection which is specified using following environment variables:

    - DATABASE_HOST
    - DATABASE_USER
    - DATABASE_PASSWORD
    - DATABASE_NAME
    - DATABASE_PORT
      
Could be started with `local` profile to use local config server `http://localhost:8888`

Use `./gradlew :words-service:docker` to build docker image

How to run:

`docker run --rm -e 'SPRING_PROFILES_ACTIVE=local' -p 8085:8085 solomkinmv/words-service`

## Documentation

Start service and visit: `http://localhost:8085/words-service/docs/index.html`