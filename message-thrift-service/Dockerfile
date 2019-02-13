FROM hub.kidding.com:8090/micro-service/java:openjdk-8-jre
MAINTAINER xxx xxx@kidding.com

COPY target/message-thrift-service-1.0-SNAPSHOT.jar /message-service.jar

ENTRYPOINT ["java", "-jar", "/message-service.jar"]