import hudson.FilePath

def repoUrl = 'git@github.com:vyevdokymenko/perf-testing-local.git'

// 1. Отримуємо об'єкт воркспейсу (FilePath), який знає, де лежать файли фізично (на агенті)
def workspace = build.workspace

if (workspace == null) {
    throw new RuntimeException("❌ КРИТИЧНА ПОМИЛКА: Не вдалося отримати доступ до воркспейсу!")
}

// 2. Визначаємо шлях до папки з пайплайнами
def pipelinesDirPath = 'pipelines'
def pipelinesDir = workspace.child(pipelinesDirPath)

// 3. Перевіряємо, чи існує папка на Агенті
if (!pipelinesDir.exists()) {
    // Викидаємо RuntimeException. Це зупинить скрипт і джоба завершиться з FAILURE
    throw new RuntimeException("❌ ПОМИЛКА: Директорія '${pipelinesDirPath}' не знайдена у репозиторії! Перевір, чи вона закоммічена.")
}

// 4. Отримуємо список файлів .jenkinsfile
def files = pipelinesDir.list('*.jenkinsfile')

if (files.length == 0) {
    println("⚠️ Попередження: Директорія '${pipelinesDirPath}' знайдена, але вона порожня або не містить файлів .jenkinsfile")
}

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