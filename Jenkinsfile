pipeline {
    agent any

    environment {
        REGISTRY = 'registry.firm.internal'
        IMAGE_NAME = 'api-health-dashboard'
    }

    stages {
        stage('Test Backend') {
            steps {
                dir('backend') {
                    sh './gradlew test'
                }
            }
        }

        stage('Test Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm ci && npm run lint && npm run build'
                }
            }
        }

        stage('Build Image') {
            steps {
                sh "docker build -t ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} ."
                sh "docker tag ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER} ${REGISTRY}/${IMAGE_NAME}:latest"
            }
        }

        stage('Push Image') {
            steps {
                sh "docker push ${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}"
                sh "docker push ${REGISTRY}/${IMAGE_NAME}:latest"
            }
        }

        stage('Deploy') {
            steps {
                sh "kubectl set image deployment/api-health-dashboard api-health-dashboard=${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}"
                sh 'kubectl rollout status deployment/api-health-dashboard --timeout=300s'
            }
        }
    }
}
