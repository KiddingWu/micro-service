FROM hub.kidding.com:8090/micro-service/java:openjdk-8-jre
MAINTAINER xxx xxx@kidding.com

COPY target/course-edge-service-1.0-SNAPSHOT.jar /course-edge-service.jar

ENTRYPOINT ["java", "-jar", "/course-edge-service.jar"]