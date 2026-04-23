package generators.mockusers


import generators.AbstractDataGenerator
import utils.Person
import utils.Utils

import java.util.concurrent.ThreadLocalRandom

class MockUsersCitizenGenerator extends AbstractDataGenerator {
    private final subjectTypes = ['ENTREPRENEUR', 'LEGAL', 'INDIVIDUAL']

    static void main(String[] args) {
        new MockUsersCitizenGenerator().generate()
    }

    @Override
    String getFileName() {
        return 'mock-users-citizen.csv'
    }

    @Override
    String getCsvHeader() {
        return 'user_name,full_name,edrpou,drfo,representative,subjectType,role'
    }

    @Override
    String generateRow(int index) {
        String userName = "perf-test-citizen-${index}"

        Person randomPerson = Utils.getRandomPerson()
        String lastName = randomPerson.lastName
        String firstName = randomPerson.firstName
        String secondName = randomPerson.secondName
        String fullName = [lastName, firstName, secondName].join(' ')
        String edrpou = ThreadLocalRandom.current().nextInt(11111111, 99999999).toString()
        String drfo = ThreadLocalRandom.current().nextLong(1111111111, 9999999999).toString()
        String subjectType = subjectTypes[ThreadLocalRandom.current().nextInt(subjectTypes.size())]
        boolean representative = subjectType == 'LEGAL'
        String role = 'unregistered-' + subjectType.toLowerCase()

        return "${userName},${fullName},${edrpou},${drfo},${representative},${subjectType},${role}"
    }
}
