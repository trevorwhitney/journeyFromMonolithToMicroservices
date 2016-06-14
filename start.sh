#!/bin/bash

mkdir -p log
mkdir -p tmp

./gradlew -b platform/serviceDiscovery/build.gradle clean build bootRun 2>&1 > log/eureka.log &
eureka_pid=$!
echo $eureka_pid > tmp/eureka.pid
echo "Starting Eurkea server with pid $eureka_pid"

./gradlew -b platform/configServer/build.gradle clean build bootRun 2>&1 > log/config.log &
config_pid=$!
echo $config_pid > tmp/config.pid
echo "Starting config server with pid $config_pid"

echo "Waiting for Eurkea server to come online"
until $(curl --output /dev/null --silent --head --fail http://localhost:8761/health); do
    printf '.'
    sleep 2
done
echo

echo "Waiting for Config server to come online"
until $(curl --output /dev/null --silent --head --fail http://localhost:8888/health); do
    printf '.'
    sleep 2
done
echo

./gradlew -b applications/billing/build.gradle clean build bootRun 2>&1 > log/billing.log &
billing_pid=$!
echo $billing_pid > tmp/billing.pid
echo "Started billing with pid ${billing_pid}"

./gradlew -b platform/circuitBreakerDashboard/build.gradle clean build bootRun 2>&1 > log/circuitBreakerDashboard.log &
circuit_pid=$!
echo $circuit_pid > tmp/circuitBreakerDashboard.pid
echo "Started circuit break dashboard with pid ${circuit_pid}"

./gradlew -b applications/ums/build.gradle clean build bootRun 2>&1 > log/ums.log &
ums_pid=$!
echo $ums_pid > tmp/ums.pid
echo "Started ums with pid ${ums_pid}"

echo "Waiting for first billing server to come online"
until $(curl --output /dev/null --silent --head --fail http://localhost:8081/health); do
    printf '.'
    sleep 2
done
echo

SPRING_PROFILES_ACTIVE=secondInstance ./gradlew -b applications/billing/build.gradle bootRun 2>&1 > log/billing_2.log &
second_billing_pid=$!
echo $second_billing_pid > tmp/billing_2.pid
echo "Started second billing with pid ${second_billing_pid}"
