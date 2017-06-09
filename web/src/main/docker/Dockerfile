FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD glossary.jar app.jar
ADD wrapper.sh wrapper.sh
RUN sh -c 'chmod +x /wrapper.sh'
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["/wrapper.sh"]