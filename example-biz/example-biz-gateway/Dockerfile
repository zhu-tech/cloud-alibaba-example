FROM openjdk:11 as builder

WORKDIR apps

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

RUN java -Djarmode=layertools -jar app.jar extract

FROM openjdk:11

RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

WORKDIR apps

ENV SKYWALKING_AGENT_PATH=/skywalking/skywalking-agent/skywalking-agent.jar
ENV SKYWALKING_OAP_URL=''
ENV SKYWALKING_SERVICE_NAME='example::gateway'

COPY --from=builder /apps/dependencies/ ./
COPY --from=builder /apps/spring-boot-loader/ ./
COPY --from=builder /apps/snapshot-dependencies/ ./
COPY --from=builder /apps/application/ ./

ENTRYPOINT if [ -n "$SKYWALKING_OAP_URL" ]; then \
    java -Xms256m -Xmx256m \
        -Djava.security.egd=file:/dev/./urandom \
        -Ddruid.mysql.usePingMethod=false \
        -javaagent:$SKYWALKING_AGENT_PATH \
        -Dskywalking.collector.backend_service=$SKYWALKING_OAP_URL \
        -Dskywalking.agent.service_name=$SKYWALKING_SERVICE_NAME \
        org.springframework.boot.loader.JarLauncher; \
else \
    java -Xms256m -Xmx256m \
        -Djava.security.egd=file:/dev/./urandom \
        -Ddruid.mysql.usePingMethod=false \
        org.springframework.boot.loader.JarLauncher; \
fi
