#!/bin/ash


# contains(string, substring)
#
# Returns 0 if the specified string contains the specified substring,
# otherwise returns 1.
contains() {
    string="$1"
    substring="$2"
    if test "${string#*$substring}" != "$string"
    then
        return 0    # $substring is in $string
    else
        return 1    # $substring is not in $string
    fi
}

db_profile="container"
contains ${SPRING_PROFILES_ACTIVE} ${db_profile} && until nc -z -v -w30 $DATABASE_HOST $DATABASE_PORT
do
  echo "Waiting for database connection..."
  # wait for 5 seconds before check again
  sleep 5
done

java -Djava.security.egd=file:/dev/./urandom -jar /app.jar