# Copied xk6-Dockerfile from: https://github.com/grafana/xk6-output-influxdb/blob/main/Dockerfile
FROM golang:1.19-alpine as k6
WORKDIR $GOPATH/src/go.k6.io/k6
ADD . .
RUN apk --no-cache add git && go install go.k6.io/xk6/cmd/xk6@latest
RUN xk6 build --with github.com/grafana/xk6-output-influxdb --output /tmp/k6

FROM maven:3.8.5-openjdk-17-slim AS build

COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN mvn install -DskipTests && mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM openjdk:17-jdk-slim

ARG DEPENDENCY=target/dependency

COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=k6 /tmp/k6 /usr/bin/k6

ENTRYPOINT ["java","-cp","app:app/lib/*","poc.ConfigRunnerApplication"]