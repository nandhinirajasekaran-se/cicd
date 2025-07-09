pipeline {
    agent any

    environment {
        TARGET_DIR = "/var/jenkins_home/workspace/ansible-deployment/target"
        JAR_NAME = "*.jar"  // Wildcard for versioned JARs
    }

    stages {
        stage('Checkout SCM') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    extensions: [[$class: 'CleanBeforeCheckout']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/nandhinirajasekaran-se/cicd.git'
                        // Remove credentialsId if using public repo
                    ]]
                ])
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                script {
                    def jarFile = findFiles(glob: "target/${JAR_NAME}")[0].path
                    dir('ansible') {
sh'''
    JAR_NAME="ansible-0.0.1-SNAPSHOT.jar"
    DEPLOY_DIR="${WORKSPACE}/deployments"

    mkdir -p "${DEPLOY_DIR}"
    echo "Copying ${TARGET_DIR}/${JAR_NAME} → ${DEPLOY_DIR}/app.jar"

    if [ ! -f "${TARGET_DIR}/${JAR_NAME}" ]; then
        echo "❌ JAR file not found at ${JAR_NAME}"
        exit 1
    fi

    cp "${TARGET_DIR}/${JAR_NAME}" "${DEPLOY_DIR}/app.jar"

    echo "Running Ansible playbook... ${WORKSPACE} "

    ansible-playbook -i inventory deploy.yml -e "workspace_dir=${WORKSPACE}"
'''
                    }
                }
            }
        }
    }

    post {
        always {
            // Only archive if build succeeded
            archiveArtifacts artifacts: "target/${JAR_NAME}", allowEmptyArchive: false
            // Optional: Remove junit if no tests are run
            // junit 'target/surefire-reports/*.xml'
        }
    }
}