# 镜像环境
FROM registry.cn-shanghai.aliyuncs.com/qianjiu_docker/openjdk:8-jdk-alpine

# 注释信息，
LABEL maintainer=it_zhangwei@163.com

# 用于指定持久化目录
VOLUME /tmp

# 环境变量
ENV PORT_TO_EXPOSE=8110

# ADD将本地文件添加到容器中，tar类型文件会自动解压(网络压缩资源不会被解压)，可以访问网络资源，类似wget
# 添加编译过后的jar到docker容器中，并重新命名
ADD ./target/*.jar ./eureka.jar

# 解决docker容器和宿主服务器的时区差问题
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 暴露端口
EXPOSE PORT_TO_EXPOSE

# 入口,运行镜像
ENTRYPOINT ['java','-Xms1g','-Xmx2g',"-Djava.security.egd=file:/dev/./urandom","-jar","/eureka.jar"]
