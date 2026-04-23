def prepLogic
def testLogic

pipeline {
    agent any

    parameters {
        string(name: 'RECORDS_COUNT', defaultValue: '3000', description: 'Кількість записів')
    }

    stages {
        stage('Init Scripts') {
            steps {
                script {
                    // Команда load зчитує файл і кладе його в змінну як об'єкт
                    prepLogic = load "pipelines/nested-entity/stages/Preparation.groovy"
                    testLogic = load "pipelines/nested-entity/stages/LoadTest.groovy"
                }
            }
        }

        stage('Preparation') {
            steps {
                script {
                    prepLogic.execute()
                }
            }
        }

        stage('Run Load Test Pipeline') {
            steps {
                script {
                    println "This stage does not implemented yet"
//                    testLogic.execute(params.RECORDS_COUNT)
                }
            }
        }
    }

    post {
        always {
            echo "Artifact collection and graph generation..."
            publishHTML([
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'html-report/nested-entity',
                    reportFiles: 'index.html',
                    reportName: 'HTML Report'
            ])
            archiveArtifacts artifacts: 'build/results/nested-entity/**/*.*', allowEmptyArchive: true
        }
    }
}