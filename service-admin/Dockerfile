FROM openjdk:8

RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone

ADD target/app.jar /app/app.jar

ENTRYPOINT ["java", "-jar","app/app.jar"]

