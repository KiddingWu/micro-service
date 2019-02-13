#!/usr/bin/env bash

mvn package

docker build -t hub.kidding.com:8090/micro-service/user-service:latest .

docker push hub.kidding.com:8090/micro-service/user-service:latest

#docker run -it user-service:latest --mysql.address=172.16.0.70