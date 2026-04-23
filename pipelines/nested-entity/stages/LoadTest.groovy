def execute(String recordsCount) {
    echo "Running the Gradle pipeline for load testing..."
    sh "./gradlew run_nested-entity -Pperf.recordsCount=${recordsCount}"
}

return this