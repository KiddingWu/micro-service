#!/usr/bin/env bash

mvn package

docker build -t hub.kidding.com:8090/micro-service/api-gateway-zuul:latest .

docker push hub.kidding.com:8090/micro-service/api-gateway-zuul:latest