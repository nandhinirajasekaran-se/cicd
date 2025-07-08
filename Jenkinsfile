pipeline {
    agent any

    environment {
        DEPLOY_DIR = '/Users/nandhinirajasekaran/Desktop/JProjects/ansible/opt/myapp' // Change this as needed
        JAR_NAME = 'ansible-0.0.1-SNAPSHOT.jar' // Match your actual jar name if different
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/nandhinirajasekaran-se/cicd.git' // Replace with your actual repo
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Deploy with Ansible') {
            steps {
                // If ansible isn't in PATH, use full path e.g. /opt/homebrew/bin/ansible-playbook
                sh 'ansible-playbook ansible/deploy.yml -i ansible/inventory'
            }
        }
    }

    post {
        success {
            echo 'Build and deployment completed!'
        }
        failure {
            echo 'Build or deploy failed. Check logs.'
        }
    }
}
