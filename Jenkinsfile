// Actividad 4 — Probar, construir la imagen Docker y hacer un smoke test
// agent none: cada etapa declara su propio agente. El cliente Docker vive en el
// nodo de Jenkins, no en el contenedor Maven.
pipeline {
    agent none

    environment {
        IMAGEN     = "taller-app:${env.BUILD_NUMBER}"
        CONTENEDOR = "taller-app-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Pruebas') {
            agent {
                docker {
                    image 'maven:3.9-eclipse-temurin-21'
                    args '--network host'   // DNS: usa la red de jenkins-docker
                }
            }
            environment { HOME = "${env.WORKSPACE}" }
            steps {
                sh 'mvn -B -Dmaven.repo.local=.m2/repository test'
            }
            post {
                always { junit 'target/surefire-reports/*.xml' }
            }
        }

        stage('Construir imagen') {
            agent any
            steps {
                // Dockerfile multi-etapa: Maven compila por dentro, la imagen final solo lleva JRE + .jar
                sh 'docker build --network host -t $IMAGEN .'
            }
        }

        stage('Smoke test') {
            agent any
            steps {
                // El contenedor corre en jenkins-docker; Jenkins lo alcanza con el nombre "docker"
                sh '''
                    docker run -d --name $CONTENEDOR -p 18000:8000 $IMAGEN
                    for i in $(seq 1 15); do
                        curl -fs http://docker:18000/health && exit 0
                        sleep 2
                    done
                    echo "La app no respondió en /health"
                    docker logs $CONTENEDOR
                    exit 1
                '''
            }
            post {
                always { sh 'docker rm -f $CONTENEDOR || true' }
            }
        }
    }

    post {
        success { echo "Imagen lista: ${env.IMAGEN}" }
    }
}
