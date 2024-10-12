pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    try {
                        git branch: 'main',
                            url: 'https://github.com/ob98x/adore-be',
                            credentialsId: 'adore_github'
                        discordSend description: "Clone Repository 성공",
                          footer: "레포지토리 복제가 성공했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Clone Repository 성공",
                          webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Clone Repository 실패",
                          footer: "레포지토리 복제에 실패했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Clone Repository 실패",
                          webhookURL: "$DISCORD"
                        throw e
                    }
                }
            }
        }

        stage('Build Services') {
            steps {
                script {
                    try {
                        // 각각의 서비스 디렉토리에서 빌드
                        dir('adore-be-eureka') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build'
                        }
                        dir('adore-be-gateway') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build'
                        }
                        dir('adore-be-first') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build'
                        }
                        dir('adore-be-second') {
                            sh 'chmod +x gradlew'
                            sh './gradlew clean build'
                        }
                        discordSend description: "Build 성공",
                          footer: "모든 서비스 빌드 성공",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Build 성공",
                          webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Build 실패",
                          footer: "서비스 빌드 실패",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Build 실패",
                          webhookURL: "$DISCORD"
                        throw e
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    try {
                        // Docker 이미지 빌드
                        dir('adore-be-eureka') {
                            sh 'docker build -t dyw1014/adore-be-eureka-service .'
                        }
                        dir('adore-be-gateway') {
                            sh 'docker build -t dyw1014/adore-be-gateway-service .'
                        }
                        dir('adore-be-first') {
                            sh 'docker build -t dyw1014/adore-be-first-service .'
                        }
                        dir('adore-be-second') {
                            sh 'docker build -t dyw1014/adore-be-second-service .'
                        }
                        discordSend description: "Docker Build 성공",
                          footer: "모든 Docker 이미지 빌드 성공",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Docker Build 성공",
                          webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Docker Build 실패",
                          footer: "Docker 이미지 빌드 실패",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Docker Build 실패",
                          webhookURL: "$DISCORD"
                        throw e
                    }
                }
            }
        }

        stage('Push Docker Images') {
            steps {
                script {
                    try {
                        // Docker 이미지 푸시
                        sh 'docker push dyw1014/adore-be-eureka-service'
                        sh 'docker push dyw1014/adore-be-gateway-service'
                        sh 'docker push dyw1014/adore-be-first-service'
                        sh 'docker push dyw1014/adore-be-second-service'
                        discordSend description: "Docker Push 성공",
                          footer: "모든 Docker 이미지 푸시 성공",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Docker Push 성공",
                          webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Docker Push 실패",
                          footer: "Docker 이미지 푸시에 실패했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Docker Push 실패",
                          webhookURL: "$DISCORD"
                        throw e
                    }
                }
            }
        }

        // 배포 스테이지
        stage('Deploy to Server') {
            steps {
                script {
                    try {
                        sshPublisher(publishers: [
                            sshPublisherDesc(configName: 'develop', transfers: [
                                sshTransfer(
                                    sourceFiles: '**/docker-compose.yml',  // docker-compose.yml 파일 전송
                                    remoteDirectory: '/home/ubuntu/adore-be',  // 서버의 디렉토리 경로
                                    execCommand: '''
                                        # 현재 활성화된 환경을 확인
                                        active_environment=$(docker ps --filter "name=gateway-service-blue" --format "{{.Names}}" | wc -l)

                                        if [ $active_environment -gt 0 ]; then
                                            new_environment="green"
                                        else
                                            new_environment="blue"
                                        fi

                                        # 새 이미지 받기
                                        docker-compose -f /home/ubuntu/adore-be/docker-compose.yml pull

                                        # 새로운 환경의 서비스를 배포
                                        docker-compose -f /home/ubuntu/adore-be/docker-compose.yml up -d \
                                            gateway-service-${new_environment} \
                                            first-service-${new_environment} \
                                            second-service-${new_environment}

                                        # 트래픽 전환을 위해 기존 환경의 서비스 중지
                                        if [ "$new_environment" = "green" ]; then
                                            docker-compose -f /home/ubuntu/adore-be/docker-compose.yml stop \
                                                gateway-service-blue \
                                                first-service-blue \
                                                second-service-blue
                                        else
                                            docker-compose -f /home/ubuntu/adore-be/docker-compose.yml stop \
                                                gateway-service-green \
                                                first-service-green \
                                                second-service-green
                                        fi
                                    '''
                                )
                            ])
                        ])
                        discordSend description: "Deploy to Server 성공",
                          footer: "서버 배포가 성공했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Deploy to Server 성공",
                          webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Deploy to Server 실패",
                          footer: "서버 배포에 실패했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Deploy to Server 실패",
                          webhookURL: "$DISCORD"
                        throw e
                    }
                }
            }
        }
    }

    post {
        failure {
            discordSend description: "빌드 실패",
              footer: "CI/CD 파이프라인 실패",
              link: env.BUILD_URL, result: currentBuild.currentResult,
              title: "빌드 실패",
              webhookURL: "$DISCORD"
        }
    }
}
