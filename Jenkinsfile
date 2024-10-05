pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/ob98x/adore-be',
                    credentialsId: 'kimyoungwoo_jenkins'
            }
        }
    }
}