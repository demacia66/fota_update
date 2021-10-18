# Dockerfile
FROM java:8
MAINTAINER xuch
ADD target/fota_update-1.0-SNAPSHOT.jar app.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java","-jar","app.jar"]