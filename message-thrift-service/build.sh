#!/usr/bin/env bash

mvn package

docker build -t message-service:latest .