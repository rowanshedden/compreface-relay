FROM openjdk:17-oracle

COPY target/*.jar application.jar

ENV PORT 8005
EXPOSE $PORT

ARG SPRING_PROFILE
RUN echo Spring profile: $SPRING_PROFILE
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILE

ENTRYPOINT ["java","-jar","/application.jar"]