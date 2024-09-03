pipeline {
    agent {label 'linux'}

    tools {
        jdk 'JDK-17'
        maven 'Maven 3.9.8'
    }

    stages {
        // Service Discovery Module
        stage('Build Service Module') {
            steps {
                dir('ServiceDiscovery') {
                    echo 'Building Service Module..'
                    sh 'mvn -v'
                    sh 'mvn clean install'
                }
            }
        }
        stage('Test Service Module') {
            steps {
                dir('ServiceDiscovery') {
                    echo 'Testing Service Module..'
                    sh 'mvn test'
                }
            }
        }

        // API Gateway module
        stage('Build API gateway Module') {
            steps {
                dir('gateway') {
                    echo 'Building API gateway Module..'
                    sh 'mvn -v'
                    sh 'mvn clean install'
                }
            }
        }
        stage('Test API gateway Module') {
            steps {
                dir('gateway') {
                    echo 'Testing API gateway Module..'
                    sh 'mvn test'
                }
            }
        }

        // TR module
        stage('Build TR Module') {
            steps {
                dir('travel-recommendation-service') {
                    echo 'Building TR Module..'
                    sh 'mvn -v'
                    sh 'mvn clean install'
                }
            }
        }
        stage('Test TR Module') {
            steps {
                dir('travel-recommendation-service') {
                    echo 'Testing TR Module..'
                    sh 'mvn test'
                }
            }
        }

        // UM module
        stage('Build UM Module') {
            steps {
                dir('user-management-service') {
                    echo 'Building UM Module..'
                    sh 'mvn -v'
                    sh 'mvn clean install'
                }
            }
        }
        stage('Test UM Module') {
            steps {
                dir('user-management-service') {
                    echo 'Testing UM Module..'
                    sh 'mvn test'
                }
            }
        }

        // Sonar Analysis
        stage('SonarQube analysis') {
            environment {
                scannerHome = tool 'SonarQube Scanner'
            }
            steps {
                withSonarQubeEnv('SonarHyd') {
                    sh '$scannerHome/bin/sonar-scanner -Dproject.settings=sonar-project.properties'
                }
            }
        }
    }
}
