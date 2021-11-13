pipeline {
    agent any

    stages {
        stage('prepare') {
            steps {
                echo 'prepare'
                sh  'ls -al'
            }
        }
        stage('build') {
            steps {
                dir('server'){
                        sh 'ls -al'
                        sh "npm install"
                }
            }
        }
        stage('deploy') {
            steps {
                sh "ls -al"
                echo 'deploy'   
            }
        }
    }
}