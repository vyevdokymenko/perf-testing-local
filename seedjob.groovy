import hudson.model.Executor
import hudson.FilePath

def repoUrl = 'git@github.com:vyevdokymenko/perf-testing-local.git'

FilePath workspace = Executor.currentExecutor().getCurrentWorkspace()

if (workspace == null) {
    throw new RuntimeException("CRITICAL ERROR: Unable to access the current agent's workspace!")
}

def pipelinesDirPath = 'pipelines'
def pipelinesDir = workspace.child(pipelinesDirPath)

if (!pipelinesDir.exists()) {
    throw new RuntimeException("ERROR: Directory '${pipelinesDirPath}' not found in repository! Workspace: ${workspace.getRemote()}")
}

def testDirs = pipelinesDir.listDirectories()
if (testDirs == null || testDirs.size() == 0) {
    println("Warning: Directory '${pipelinesDirPath}' found, but it is empty or does not contain .jenkinsfile files")
} else {
    testDirs.each { testDir ->
        def testName = testDir.getName()
        def jenkinsfile = testDir.child('Jenkinsfile')
        if (jenkinsfile.exists()) {
            pipelineJob("perf-test-${testName}") {
                description("Automatically generated pipeline for performance testing: ${testName}")

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
                        scriptPath("${pipelinesDirPath}/${file.name}")
                    }
                }
            }
            println("Job successfully created/updated: perf-test-${testName}")
        } else {
            println("Skipped folder '${testName}': no Jenkinsfile found inside")
        }
    }
}
