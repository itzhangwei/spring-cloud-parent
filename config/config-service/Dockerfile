FROM registry.cn-shanghai.aliyuncs.com/qianjiu_docker/openjdk:8-jdk-alpine

VOLUME /temp

ENV PORT_TO_EXPOSE=8210

ADD ./target/*.jar /config-service.jar


RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

EXPOSE $PORT_TO_EXPOSE

ENTRYPOINT ["java","-Xms1g","-Xmx2g","-Djava.security.egd=file:/dev/./urandom","-jar","/config-service.jar"]



