# TTS service

TTS Service (Text-to-speech) returns url to speech audio record. 

Service checks `storage-service` if it contains necessary speech record. If there is no such record 
service create speech record using Amazon Polly and saved the record to `storage-service`.

Requires config server to be up and running `http://config:8888`.
Requires Amazon Web Services API keys via following environment variables:

    - AWS_SECRET_ACCESS_KEY
    - AWS_ACCESS_KEY_ID

Could be started with `local` profile to use local config server `http://localhost:8888`

Use `./gradlew :tts-service:docker` to build docker image

How to run:

`docker run --rm -e 'SPRING_PROFILES_ACTIVE=local' -p 8083:8083 solomkinmv/tts-service`

Use `stub` profile to stub Amazon Polly requests.

## Documentation

Start service and visit: `http://localhost:8083/tts-service/docs/index.html`