#!/bin/bash

mkdir -p log
mkdir -p tmp

./gradlew -b platform/serviceDiscovery/build.gradle clean build bootRun > log/eureka.log &
eureka_pid=$!
echo $eureka_pid > tmp/eureka.pid
echo "Starting Eurkea server with pid $eureka_pid"

./gradlew -b platform/configServer/build.gradle clean build bootRun > log/config.log &
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

./gradlew -b platform/circuitBreakerDashboard/build.gradle clean build bootRun > log/circuitBreakerDashboard.log &
circuit_pid=$!
echo $circuit_pid > tmp/circuitBreakerDashboard.pid
echo "Started circuit break dashboard with pid ${circuit_pid}"

