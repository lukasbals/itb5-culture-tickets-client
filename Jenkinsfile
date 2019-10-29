pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                sh 'mvn test'
                junit 'target/surefire-reports/*.xml'
            }
        }
        stage('Build') {
            when {
                branch 'master'
            }
            steps {
                sh 'mvn clean -DskipTests -U package'
            }
        }
    }
}
