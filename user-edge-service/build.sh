#!/usr/bin/env bash

mvn package

docker build -t user-edge-service:latest .