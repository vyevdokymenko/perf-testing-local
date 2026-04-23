import jenkins.model.Jenkins
import hudson.model.Cause

def jobName = "seed-job-pipeline-generator"
def job = Jenkins.instance.getItemByFullName(jobName)

if (job != null) {
    println "--- [INIT SCRIPT] Autostart Seed Job when Jenkins starts ---"
    if (!job.isInQueue() && !job.isBuilding()) {
        job.scheduleBuild2(0, new Cause.UserIdCause())
        println "--- [INIT SCRIPT] Seed Job successfully added to the queue ---"
    }
} else {
    println "--- [INIT SCRIPT] Error: Job ${jobName} not found ---"
}