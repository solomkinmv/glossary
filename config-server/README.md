# Configuration service

Read config files from `http://github.com/solomkinmv/glossary/configs`.

Could be started with `native` profile to use config files from `classpath:/shared`

Use `./gradlew :config-server:docker` to build docker image

How to run:

`docker run --rm -e 'SPRING_PROFILES_ACTIVE=native' -p 8888:8888 solomkinmv/config-server   `