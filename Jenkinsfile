pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/ob98x/adore-be',
                    credentialsId: 'adore_github'
            }
        }

        stage('Set Gradle Permissions') {
            steps {
                sh 'chmod +x gradlew'
            }
        }

        stage('Build Project') {
            steps {
                sh './gradlew clean build'
            }
        }

        stage('Docker Login') {
            steps {
                script {
                    sh 'echo $DOCKER_PASSWORD | docker login -u $DOCKER_USERNAME --password-stdin'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t dyw1014/adore-be .'
            }
        }

        stage('Push Docker Image') {
            steps {
                sh 'docker push dyw1014/adore-be'
            }
        }
    }
}
