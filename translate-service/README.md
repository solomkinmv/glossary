# Translate service

Service translates text from source to target language. Source language could be omitted and service would try to guess.

Requires config server to be up and running `http://config:8888`.
Requires Yandex Translate API key via following environment variable:

    - TRANSLATE_KEY

Could be started with `local` profile to use local config server `http://localhost:8888`

Use `./gradlew :translate-service:docker` to build docker image

How to run:

`docker run --rm -e 'SPRING_PROFILES_ACTIVE=local' -p 8082:8082 solomkinmv/translate-service`

## Documentation

Start service and visit: `http://localhost:8082/translate-service/docs/index.html`