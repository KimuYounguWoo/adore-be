pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                script {
                    try {
                        git branch: 'main',
                            url: 'https://github.com/ob98x/adore-be',
                            credentialsId: 'KimYoungWoo'

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
                        def services = ['discovery-service', 'gateway-service', 'user-service', 'community-service']
                        services.each { service ->
                            dir(service) {
                                sh 'chmod +x gradlew'
                                sh './gradlew clean build'
                            }
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
                        def services = ['discovery-service', 'gateway-service', 'user-service', 'community-service']
                        services.each { service ->
                            dir(service) {
                                sh "docker build --no-cache -t dyw1014/adore-be-${service} ."
                            }
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
                        def services = ['discovery-service', 'gateway-service', 'user-service', 'community-service']
                        services.each { service ->
                            sh "docker push dyw1014/adore-be-${service}"
                        }

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

        stage('Deploy to Server') {
            steps {
                script {
                    try {
                        sshPublisher(publishers: [
                            sshPublisherDesc(configName: 'develop', transfers: [
                                sshTransfer(
                                    execCommand: '''
                                        # 프로젝트 디렉토리로 이동
                                        cd /home/ubuntu/adore-be

                                        # Git 저장소에서 최신 코드 가져오기
                                        git pull origin main

                                        # 최신 이미지 가져오기
                                        docker-compose pull

                                        # 새로운 서비스 배포
                                        docker-compose up -d \
                                            community-service \
                                            user-service \
                                            gateway-service
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
