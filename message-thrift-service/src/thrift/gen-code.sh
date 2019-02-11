#!/usr/bin/env bash
thrift --gen java -out ../../../message-thrift-service-api/src/main/java message.thrift
