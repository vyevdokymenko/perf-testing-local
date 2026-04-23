package generators.mockusers

import config.PerfConfig
import generators.AbstractDataGenerator
import utils.Constants
import utils.Person
import utils.Utils

import java.util.concurrent.ThreadLocalRandom

import static utils.Constants.getDEPLOY_TEMPLATES_DIR
import static utils.Constants.getTEST_DATA_DIR

class MockUsersGenerator extends AbstractDataGenerator {
    static void main(String[] args) {
        new MockUsersGenerator().generate()
    }

    @Override
    String getFileName() {
        return 'mock-users.csv'
    }

    @Override
    String getCsvHeader() {
        return 'user_name,full_name,edrpou,drfo'
    }

    @Override
    String generateRow(int index) {
        String userName = "perf-test-officer-${index}"

        Person randomPerson = Utils.getRandomPerson()
        String lastName = randomPerson.lastName
        String firstName = randomPerson.firstName
        String secondName = randomPerson.secondName
        String fullName = [lastName, firstName, secondName].join(' ')
        String edrpou = ThreadLocalRandom.current().nextInt(11111111, 99999999).toString()
        String drfo = ThreadLocalRandom.current().nextLong(1111111111, 9999999999).toString()

        return "${userName},${fullName},${edrpou},${drfo}"
    }

    @Override
    void postGenerateTask() {
        def srcDir = PerfConfig.getProperty(Constants.TEST_DATA_DIR, String)
        def source = Utils.getOutputFile(srcDir, getFileName()).toPath()
        def outDir = PerfConfig.getProperty(Constants.DEPLOY_TEMPLATES_DIR, String)
        def output = Utils.getOutputFile(outDir, 'mock-users.tar.gz').toPath()
        Utils.createTarGz(source, output)
    }
}
