package generators

import config.PerfConfig
import utils.Constants
import utils.Utils

import static utils.Constants.*

abstract class AbstractDataGenerator {
    abstract String getFileName()
    abstract String getCsvHeader()
    abstract String generateRow(int index)
    void postGenerateTask() {}

    void generate() {
        int recordsCount = PerfConfig.getProperty(Constants.RECORDS_COUNT, Integer)
        def outputDir = PerfConfig.getProperty(Constants.TEST_DATA_DIR, String)
        def outputFile = Utils.getOutputFile(outputDir, getFileName())
        println "Starting generation of ${recordsCount} records..."

        outputFile.withWriter('UTF-8') { writer ->
            writer.writeLine(getCsvHeader())
            for (int i = 1; i <= recordsCount; i++) {
                writer.writeLine(generateRow(i))
            }
        }
        println "Successfully generated file: ${outputFile.absolutePath}"

        postGenerateTask()
    }
}
