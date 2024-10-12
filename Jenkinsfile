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

        // 각 서비스 빌드
        stage('Build Services') {
            steps {
                script {
                    try {
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

        // 각 서비스 Docker 이미지 빌드
        stage('Build Docker Images') {
            steps {
                script {
                    try {
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

        // Docker 이미지 푸시
        stage('Push Docker Images') {
            steps {
                script {
                    try {
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

//         // Blue-Green 배포
//         stage('Blue-Green Deployment') {
//             steps {
//                 script {
//                     try {
//                         // 현재 활성화된 환경을 확인
//                         def activeEnvironment = sh(script: 'docker ps --filter "name=gateway-service-blue" --format "{{.Names}}"', returnStdout: true).trim() ? 'blue' : 'green'
//                         def newEnvironment = activeEnvironment == 'blue' ? 'green' : 'blue'
//
//                         // 새로운 환경 배포
//                         sh '''
//                         docker-compose up -d gateway-service-${newEnvironment} first-service-${newEnvironment} second-service-${newEnvironment}
//                         '''
//
//                         // 트래픽 전환
//                         sh '''
//                         if [ "$activeEnvironment" == "blue" ]; then
//                             docker-compose stop gateway-service-blue
//                             docker-compose stop first-service-blue
//                             docker-compose stop second-service-blue
//                         else
//                             docker-compose stop gateway-service-green
//                             docker-compose stop first-service-green
//                             docker-compose stop second-service-green
//                         fi
//                         '''
//
//                         discordSend description: "${newEnvironment} 환경 배포 성공",
//                             footer: "${newEnvironment} 환경 배포 성공",
//                             link: env.BUILD_URL, result: currentBuild.currentResult,
//                             title: "${newEnvironment} 환경 배포 성공",
//                             webhookURL: "$DISCORD"
//                     } catch (Exception e) {
//                         discordSend description: "${newEnvironment} 환경 배포 실패",
//                             footer: "${newEnvironment} 환경 배포 실패",
//                             link: env.BUILD_URL, result: currentBuild.currentResult,
//                             title: "${newEnvironment} 환경 배포 실패",
//                             webhookURL: "$DISCORD"
//                         throw e
//                     }
//                 }
//             }
//         }
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
