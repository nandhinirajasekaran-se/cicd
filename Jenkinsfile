pipeline {
    agent any

    // Webhook trigger configuration
    triggers {
        githubPush()  // Requires GitHub plugin
    }

    environment {
        // Customize these values
        DEPLOY_DIR = "${WORKSPACE}/deployments"
        JAR_NAME = "your-app-*.jar"  // Wildcard to match versioned JARs
        ANSIBLE_DIR = "ansible"
    }

    stages {
        // Stage 1: SCM Checkout
        stage('Checkout Code') {
            steps {
                checkout scm  // Automatically checks out the triggering commit
            }
        }

        // Stage 2: Maven Build
        stage('Build with Maven') {
            steps {
                sh '''
                    chmod +x mvnw
                    ./mvnw clean package -DskipTests
                '''
            }
            post {
                success {
                    archiveArtifacts "target/${JAR_NAME}"  // Archive the built JAR
                }
            }
        }

        // Stage 3: Ansible Deployment
        stage('Deploy with Ansible') {
            steps {
                script {
                    // Verify JAR exists
                    def jarFile = findFiles(glob: "target/${JAR_NAME}")[0].path

                    dir(ANSIBLE_DIR) {
                        // Copy JAR to Ansible accessible location
                        sh "mkdir -p ${DEPLOY_DIR}"
                        sh "cp ../${jarFile} ${DEPLOY_DIR}/app.jar"

                        // Execute Ansible
                        sh "ansible-playbook -i inventory deploy.yml -vvv"
                    }
                }
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'  // Publish test reports
            cleanWs()  // Clean workspace after build
        }
        success {
            slackSend(
                color: 'good',
                message: "SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n" +
                         "Commit: ${env.GIT_COMMIT.take(8)}\n" +
                         "Branch: ${env.GIT_BRANCH}"
            )
        }
        failure {
            slackSend(
                color: 'danger',
                message: "FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}\n" +
                         "See: ${env.BUILD_URL}console"
            )
        }
    }
}