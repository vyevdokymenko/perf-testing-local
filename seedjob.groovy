import hudson.model.Executor
import hudson.FilePath

def repoUrl = 'git@github.com:vyevdokymenko/perf-testing-local.git'

// 1. Отримуємо воркспейс безпосередньо з поточного виконавця (Агента), який запустив цю джобу
FilePath workspace = Executor.currentExecutor().getCurrentWorkspace()

if (workspace == null) {
    throw new RuntimeException("❌ КРИТИЧНА ПОМИЛКА: Не вдалося отримати доступ до воркспейсу поточного агента!")
}

// 2. Визначаємо шлях до папки з пайплайнами
def pipelinesDirPath = 'pipelines'
def pipelinesDir = workspace.child(pipelinesDirPath)

// 3. Перевіряємо, чи існує папка на Агенті
if (!pipelinesDir.exists()) {
    throw new RuntimeException("❌ ПОМИЛКА: Директорія '${pipelinesDirPath}' не знайдена у репозиторії! Воркспейс: ${workspace.getRemote()}")
}

// 4. Отримуємо список файлів .jenkinsfile
def files = pipelinesDir.list('*.jenkinsfile')

if (files == null || files.length == 0) {
    println("⚠️ Попередження: Директорія '${pipelinesDirPath}' знайдена, але вона порожня або не містить файлів .jenkinsfile")
} else {
    // 5. Генеруємо джоби
    files.each { file ->
        def testName = file.name.replace('.jenkinsfile', '')

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
                            branches('main')
                        }
                    }
                    // Шлях до самого дженкінсфайла всередині репозиторію
                    scriptPath("${pipelinesDirPath}/${file.name}")
                }
            }
        }
        println("✅ Успішно створено/оновлено джобу: perf-test-${testName}")
    }
}