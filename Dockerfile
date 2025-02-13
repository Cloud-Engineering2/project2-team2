FROM openjdk:17

# 1. KST로 시간대 설정 (Linux 환경)
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 2. JAR 파일 복사
ARG JAR_FILE=build/libs/app.jar
COPY ${JAR_FILE} app.jar

# 3. JVM에서 KST 적용
ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-jar", "/app.jar"]