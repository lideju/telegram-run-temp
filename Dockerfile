#基础镜像
FROM openjdk:8

#把当前目录下的jar包拷贝进docker镜像里
COPY *.jar /telegram-etl-docker.jar

#对外暴露8080端口
EXPOSE 8080

#ENTRYPOINT命令用于指定这个容器启动的时候要运行的命令(可以追加命令)
ENTRYPOINT  ["java","-jar","/telegram-etl-docker.jar","--spring.profiles.active=pro","--logging.level.root=warn","--cache.path=/tmp/telegram/","--kafka.group=TELEGRAM_CRAWLERDATA_PRODUCTION_GROUP","--server.port=8090"]