# Hystrix Dashboard

Hystrix Dashboard aggregates all hystrix streams and displays them.

Requires config server to be up and running `http://config:8888`.

Could be started with `local` profile to use local config server `http://localhost:8888`

Use `./gradlew :dashboard:docker` to build docker image.

How to run:

`docker run --rm -e 'SPRING_PROFILES_ACTIVE=local' -p 9001:9001 solomkinmv/dashboard`

Visit dashboard: `http://localhost:9001/hystrix`.

Use turbine stream to open dashboard: `http://localhost:9001/turbine.stream`.