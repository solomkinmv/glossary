# Image Service

Image Service provides methods to search for images in Flickr and to store them in storage service.

Service uses `storage-service` to store images.

Requires config server to be up and running `http://config:8888`.
Requires Flickr API keys via following environment variables:

    - FLICKR_KEY
    - FLICKR_SECRET

Could be started with `local` profile to use local config server `http://localhost:8888`

Use `./gradlew :image-service:docker` to build docker image.

How to run:

`docker run --rm -e 'SPRING_PROFILES_ACTIVE=local' -p 8084:8084 solomkinmv/image-service`

## Documentation

Start service and visit: `http://localhost:8084/image-service/docs/index.html`