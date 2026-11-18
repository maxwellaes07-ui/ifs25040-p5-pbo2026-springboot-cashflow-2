pipeline {
    agent any
    stages {
        stage('Build & Test') { steps { sh 'mvn clean verify' } }
        stage('Package') { steps { sh 'mvn package -DskipTests' } }
    }
}
