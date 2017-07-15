FROM openjdk:8-jre-alpine
ADD build/libs/user.jar /app/user.jar
EXPOSE 8080
CMD java -jar /app/user.jar --connection=cont_postgresql