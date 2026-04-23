package generators.datafactory

import generators.AbstractDataGenerator
import utils.Person
import utils.Utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom

class NestedEntityDataGenerator extends AbstractDataGenerator {
    private final def dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private final String orderDate = LocalDateTime.now().format(dateFormatter)
    private final List<String> orderTypes = ["ENROLLMENT", "TRANSFER", "DISMISSAL"]

    static void main(String[] args) {
        new NestedEntityDataGenerator().generate()
    }

    @Override
    String getFileName() {
        return 'nested-entity-execution-plan.csv'
    }

    @Override
    String getCsvHeader() {
        return 'user_name,last_name,first_name,second_name,birthday,order_type,order_number,order_date,file'
    }

    @Override
    String generateRow(int index) {
        String userName = "perf-test-officer-${index}"

        Person randomPerson = Utils.getRandomPerson()
        String lastName = randomPerson.lastName
        String firstName = randomPerson.firstName
        String secondName = randomPerson.secondName
        String birthday = randomPerson.birthday

        String orderType = orderTypes[ThreadLocalRandom.current().nextInt(orderTypes.size())]
        String orderNumber = ThreadLocalRandom.current().nextInt(1111, 9999).toString()

        String file = ''

        return  "${userName},${lastName},${firstName},${secondName},${birthday},${orderType},${orderNumber},${orderDate},${file}"
    }
}