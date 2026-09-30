pipeline {
    agent any

    parameters {
        string(
            name: 'API_HEALTH_URL',
            defaultValue: 'http://100.83.72.100:9999/criandoAPI/v1/actuator/health',
            description: 'Health check da API sob teste (https://github.com/JeanHeberth/criandoAPI), já implantada no Tomcat pela pipeline da API'
        )
    }

    environment {
        // O baseUri/basePath usados pelos testes ficam em
        // src/main/resources/dev-properties.yaml e devem apontar para a mesma API
        // validada pelo health check acima.
        API_HEALTH_URL = "${params.API_HEALTH_URL}"
    }

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {

        stage('Checkout - Projeto de Testes') {
            steps {
                echo 'Clonando repositório de testes (TestNG + RestAssured + Allure)...'
                checkout scm
            }
        }

        stage('Validar Java') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            echo "Java do agente Jenkins:"
                            java -version

                            echo "JDKs reconhecidos pelo Gradle:"
                            chmod +x gradlew
                            ./gradlew -q javaToolchains --no-daemon
                        '''
                    } else {
                        bat '''
                            @echo off

                            echo Java do agente Jenkins:
                            java -version

                            echo JDKs reconhecidos pelo Gradle:
                            call gradlew.bat -q javaToolchains --no-daemon
                        '''
                    }
                }
            }
        }

        stage('Aguardar API estar disponível') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            echo "Aguardando API em $API_HEALTH_URL..."

                            for i in $(seq 1 24); do
                                echo "Tentativa $i de 24..."

                                if curl --silent --fail "$API_HEALTH_URL" > /dev/null; then
                                    echo "API disponível."
                                    exit 0
                                fi

                                echo "API ainda não está disponível."
                                sleep 5
                            done

                            echo "ERRO: API não respondeu no tempo esperado."
                            exit 1
                        '''
                    } else {
                        bat '''
                            @echo off

                            echo Aguardando API em %API_HEALTH_URL%...

                            for /L %%i in (1,1,24) do (
                                echo Tentativa %%i de 24...

                                curl.exe --silent --fail "%API_HEALTH_URL%" >nul 2>&1

                                if not errorlevel 1 (
                                    echo API disponivel.
                                    exit /b 0
                                )

                                echo API ainda nao esta disponivel.

                                powershell.exe -NoProfile -Command "Start-Sleep -Seconds 5"
                            )

                            echo ERRO: API nao respondeu no tempo esperado.
                            exit /b 1
                        '''
                    }
                }
            }
        }

        stage('Preparar Projeto de Testes') {
            steps {
                script {
                    if (isUnix()) {
                        sh '''
                            chmod +x gradlew

                            ./gradlew clean testClasses \
                                --stacktrace \
                                --no-daemon
                        '''
                    } else {
                        bat '''
                            @echo off

                            call gradlew.bat clean testClasses ^
                                --stacktrace ^
                                --no-daemon

                            exit /b %ERRORLEVEL%
                        '''
                    }
                }
            }
        }

        stage('Executar Testes') {
            steps {
                // Mantém o pipeline seguindo para publicar os relatórios mesmo
                // quando há testes falhando; o build é marcado como FAILURE.
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    script {
                        if (isUnix()) {
                            sh '''
                                echo "Executando testes de API..."

                                ./gradlew test \
                                    --stacktrace \
                                    --no-daemon
                            '''
                        } else {
                            bat '''
                                @echo off

                                echo Executando testes de API...

                                call gradlew.bat test ^
                                    --stacktrace ^
                                    --no-daemon

                                exit /b %ERRORLEVEL%
                            '''
                        }
                    }
                }
            }
        }

        stage('Publicar Resultados JUnit') {
            steps {
                junit(
                    allowEmptyResults: true,
                    testResults: '**/build/test-results/**/*.xml'
                )
            }
        }

        stage('Publicar Allure Report') {
            steps {
                allure(
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'build/allure-results']]
                )
            }
        }
    }

    post {
        success {
            echo 'Suite de testes aprovada.'
        }

        unstable {
            echo 'Pipeline concluída com testes instáveis/falhos.'
        }

        failure {
            echo 'Falha na pipeline de testes.'
        }

        always {
            archiveArtifacts(
                artifacts: '**/build/reports/tests/**',
                allowEmptyArchive: true
            )

            archiveArtifacts(
                artifacts: '**/build/allure-results/**',
                allowEmptyArchive: true
            )

            echo 'Pipeline de testes concluída.'
        }
    }
}
