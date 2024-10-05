FROM openjdk:17

# Docker CLI 설치
RUN apt-get update && apt-get install -y \
    docker.io \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/*

# JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 포트 노출
EXPOSE 8080

# 엔트리 포인트 설정
ENTRYPOINT ["java", "-jar", "app.jar"]

# 사용 예: 이 컨테이너를 실행할 때 Docker 소켓을 공유해야 합니다.
# docker run -d \
#   -v /var/run/docker.sock:/var/run/docker.sock \  # Docker 소켓 공유
#   -p 8080:8080 \  # 포트 매핑
#   your_image_name  # 실행할 이미지 이름
