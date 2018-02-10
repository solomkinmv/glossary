# Storage service

Storage service used to store, get and delete images or sound files on file system or AWS S3.

Requires config server to be up and running `http://config:8888`.
Could be started with `local` profile to use local config server `http://localhost:8888`

Use `./gradlew :storage-service:docker` to build docker image

How to run:

`docker run --rm -e 'SPRING_PROFILES_ACTIVE=local' -p 8081:8081 solomkinmv/storage-service`

## Documentation

Start service and visit: `http://localhost:8081/storage-service/docs/index.html`