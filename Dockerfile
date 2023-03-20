FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src ./src

COPY wait-for-it.sh .
RUN chmod +x wait-for-it.sh

CMD ["./mvnw", "spring-boot:run"]