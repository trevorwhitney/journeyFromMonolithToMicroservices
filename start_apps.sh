#!/bin/bash

./gradlew -b applications/billing/build.gradle clean build bootRun > log/billing.log &
billing_pid=$!
echo $billing_pid > tmp/billing.pid
echo "Started billing with pid ${billing_pid}"

./gradlew -b applications/ums/build.gradle clean build bootRun > log/ums.log &
ums_pid=$!
echo $ums_pid > tmp/ums.pid
echo "Started ums with pid ${ums_pid}"

echo "Waiting for first billing server to come online"
until $(curl --output /dev/null --silent --head --fail http://localhost:8081/health); do
    printf '.'
    sleep 2
done
echo

SPRING_PROFILES_ACTIVE=secondInstance ./gradlew -b applications/billing/build.gradle bootRun > log/billing_2.log &
second_billing_pid=$!
echo $second_billing_pid > tmp/billing_2.pid
echo "Started second billing with pid ${second_billing_pid}"
