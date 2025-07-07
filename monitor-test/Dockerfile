# 멀티스테이지 빌드 사용
FROM amazoncorretto:17-alpine as builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle 래퍼와 빌드 파일 복사
COPY gradle gradle
COPY gradlew .
COPY gradle.properties* ./
COPY build.gradle* .
COPY settings.gradle* .

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 의존성 다운로드 (캐시 활용)
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./gradlew bootJar --no-daemon

# 런타임 이미지
FROM amazoncorretto:17-alpine

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]