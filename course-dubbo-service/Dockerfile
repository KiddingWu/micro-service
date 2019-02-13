FROM hub.kidding.com:8090/micro-service/java:openjdk-8-jre
MAINTAINER xxx xxx@kidding.com

COPY target/course-dubbo-service-1.0-SNAPSHOT.jar /course-service.jar

ENTRYPOINT ["java", "-jar", "/course-service.jar"]
