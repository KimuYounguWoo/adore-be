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

        stage('Set Gradle Permissions') {
            steps {
                script {
                    try {
                        sh 'chmod +x gradlew'
                        discordSend description: "Set Gradle Permissions 성공",
                          footer: "Gradle 권한 설정이 성공했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Set Gradle Permissions 성공",
                          webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Set Gradle Permissions 실패",
                          footer: "Gradle 권한 설정에 실패했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Set Gradle Permissions 실패",
                          webhookURL: "$DISCORD"

                        throw e
                    }
                }
            }
        }

        stage('Build Project') {
            steps {
                script {
                    try {
                        sh './gradlew clean build'
                        discordSend description: "Build Project 성공",
                          footer: "프로젝트 빌드가 성공했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Build Project 성공",
                           webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Build Project 실패",
                          footer: "프로젝트 빌드에 실패했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Build Project 실패",
                          webhookURL: "$DISCORD"
                        throw e
                    }
                }
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    try {
                        sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                        discordSend description: "Docker Login 성공",
                          footer: "Docker 로그인에 성공했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Docker Login 성공",
                          webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Docker Login 실패",
                          footer: "Docker 로그인에 실패했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Docker Login 실패",
                          webhookURL: "$DISCORD"
                        throw e
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    try {
                        sh 'docker build -t dyw1014/adore-be .'
                        discordSend description: "Build Docker Image 성공",
                          footer: "Docker 이미지 빌드가 성공했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Build Docker Image 성공",
                          webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Build Docker Image 실패",
                          footer: "Docker 이미지 빌드에 실패했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Build Docker Image 실패",
                          webhookURL: "$DISCORD"
                        throw e
                    }
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    try {
                        sh 'docker push dyw1014/adore-be'
                        discordSend description: "Push Docker Image 성공",
                          footer: "Docker 이미지 푸시가 성공했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Push Docker Image 성공",
                          webhookURL: "$DISCORD"
                    } catch (Exception e) {
                        discordSend description: "Push Docker Image 실패",
                          footer: "Docker 이미지 푸시에 실패했습니다.",
                          link: env.BUILD_URL, result: currentBuild.currentResult,
                          title: "Push Docker Image 실패",
                          webhookURL: "$DISCORD"
                        throw e
                    }
                }
            }
        }
    }

    post {
        success {
            discordSend description: "알림테스트",
              footer: "테스트 빌드가 성공했습니다.",
              link: env.BUILD_URL, result: currentBuild.currentResult,
              title: "테스트 젠킨스 job",
              webhookURL: "$DISCORD"
        }
        failure {
            discordSend description: "알림테스트",
              footer: "테스트 빌드가 실패했습니다.",
              link: env.BUILD_URL, result: currentBuild.currentResult,
              title: "테스트 젠킨스 job",
              webhookURL: "$DISCORD"
        }
    }
}
