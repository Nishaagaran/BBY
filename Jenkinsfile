pipeline {
    agent any
    
    tools {
        // Configure Maven and JDK versions
        maven 'Maven'
        jdk 'JDK-17'
    }
    
    environment {
        // Set Java version
        JAVA_HOME = "${tool 'java17'}"
        MAVEN_HOME = "${tool 'Maven3'}"
        PATH = "${MAVEN_HOME}/bin:${JAVA_HOME}/bin:${env.PATH}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code from repository...'
                checkout scm
                script {
                    def gitCommit = sh(returnStdout: true, script: 'git rev-parse HEAD').trim()
                    def gitBranch = sh(returnStdout: true, script: 'git rev-parse --abbrev-ref HEAD').trim()
                    echo "Git Branch: ${gitBranch}"
                    echo "Git Commit: ${gitCommit}"
                }
            }
        }
        
        stage('Build') {
            steps {
                echo 'Building the project with Maven...'
                sh 'mvn clean compile'
            }
            post {
                success {
                    echo 'Build completed successfully!'
                }
                failure {
                    echo 'Build failed!'
                    error('Build stage failed')
                }
            }
        }
        
        stage('Test') {
            steps {
                echo 'Running unit tests...'
                sh 'mvn test'
            }
            post {
                always {
                    // Publish test results
                    junit 'target/surefire-reports/*.xml'
                }
                success {
                    echo 'All tests passed!'
                }
                failure {
                    echo 'Tests failed!'
                    error('Test stage failed')
                }
            }
        }
    }
    
    post {
        always {
            echo 'Pipeline execution completed.'
            // Clean up workspace if needed
            cleanWs()
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
        unstable {
            echo 'Pipeline is unstable!'
        }
    }
}

