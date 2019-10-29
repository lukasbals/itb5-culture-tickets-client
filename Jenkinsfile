pipeline {
    agent any

    stages {
        stage('Test') {
            steps {
                sh 'mvn test'
                sh 'mvn sonar:sonar \
					  -Dsonar.projectKey=BakaBoing_itb5-culture-tickets-client \
					  -Dsonar.organization=bakaboing \
					  -Dsonar.host.url=https://sonarcloud.io \
					  -Dsonar.login=2cc706372813b827a5ae052109b4b51dc79c002c'
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
