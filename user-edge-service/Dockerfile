FROM hub.kidding.com:8090/micro-service/java:openjdk-8-jre
MAINTAINER xxx xxx@kidding.com

COPY target/user-edge-service-1.0-SNAPSHOT.jar /user-edge-service.jar

ENTRYPOINT ["java", "-jar", "/user-edge-service.jar"]