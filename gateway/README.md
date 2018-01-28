# Gateway service

Proxies all call to other services. You can access other service via following url: 
`http://gateway:8080/{service_name}/{service_request}`.

Requires config server to be up and running `http://config:8888`.
Could be started with `local` profile to use local config server `http://localhost:8888`

Use `./gradlew :gateway:docker` to build docker image

How to run:

`docker run --rm -e 'SPRING_PROFILES_ACTIVE=local' -p 8080:8080 solomkinmv/gateway`