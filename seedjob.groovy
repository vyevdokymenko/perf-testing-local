// Файл: seedjob.groovy (має лежати в корені репозиторію)
def repoUrl = 'git@github.com:vyevdokymenko/perf-testing-local.git'

// Отримуємо шлях до воркспейсу, куди Jenkins щойно зробив git clone
def workspacePath = getProperty('WORKSPACE')

// Вказуємо папку, де лежать наші пайплайни.
// (Якщо ти залишив папку camunda-tests, зміни на 'camunda-tests/pipelines')
def pipelinesDirPath = 'pipelines'
def pipelinesDir = new File(workspacePath, pipelinesDirPath)

// Перевіряємо, чи існує папка
if (pipelinesDir.exists()) {

    // Шукаємо всі файли, що закінчуються на .jenkinsfile
    pipelinesDir.eachFileMatch(~/.*\.jenkinsfile/) { file ->

        // Витягуємо ім'я тесту (наприклад, з "nested-entity.jenkinsfile" отримаємо "nested-entity")
        def testName = file.name.replace('.jenkinsfile', '')

        // Генеруємо джобу типу Pipeline
        pipelineJob("perf-test-${testName}") {
            description("Автоматично згенерований пайплайн для навантажувального тесту: ${testName}")

            definition {
                cpsScm {
                    scm {
                        git {
                            remote {
                                url(repoUrl)
                                credentials('github-ssh-key')
                            }
                            // Якщо твоя головна гілка називається master, а не main - зміни тут!
                            branches('main')
                        }
                    }
                    // Вказуємо Jenkins, де лежить файл пайплайну для цієї конкретної джоби
                    scriptPath("${pipelinesDirPath}/${file.name}")
                }
            }
        }
        println("✅ Створено джобу: perf-test-${testName}")
    }
} else {
    println("❌ Директорія ${pipelinesDirPath} не знайдена у репозиторії! Перевір структуру папок.")
}