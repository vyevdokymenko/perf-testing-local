package utils

class Person {
    def lastName
    def firstName
    def secondName
    def birthday

    Person(firstName, lastName, secondName, birthday) {
        this.firstName = firstName
        this.lastName = lastName
        this.secondName = secondName
        this.birthday = birthday
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("lastName=").append(lastName);
        sb.append(", firstName=").append(firstName);
        sb.append(", secondName=").append(secondName);
        sb.append(", birthday=").append(birthday);
        sb.append('}');
        return sb.toString();
    }
}
