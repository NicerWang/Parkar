FROM nginx:1.20
RUN rm -f /etc/localtime \
&& ln -sv /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
&& echo "Asia/Shanghai" > /etc/timezone
COPY ./dist/ /usr/share/nginx/html/
COPY ./nginx/ /etc/nginx/
EXPOSE 80

