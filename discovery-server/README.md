# Discovery service

Read config files from `http://github.com/solomkinmv/glossary/configs`.

Requires config server to be up and running `http://config:8888`.
Could be started with `local` profile to use local config server `http://localhost:8888`

Use `./gradlew :discovery-server:docker` to build docker image

How to run:

`docker run --rm -e 'SPRING_PROFILES_ACTIVE=local' -p 8888:8888 solomkinmv/discovery-server   `