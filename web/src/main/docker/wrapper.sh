#!/bin/ash

until nc -z -v -w30 $DATABASE_HOST $DATABASE_PORT
do
  echo "Waiting for database connection..."
  # wait for 5 seconds before check again
  sleep 5
done

java -Djava.security.egd=file:/dev/./urandom -jar /app.jar