package pl.training.performance.serialization;

import java.io.*;
import java.time.LocalDate;

public class SerializationTest {

    private static final String OUTPUT_FILE = "person.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            var person = Person.builder()
                    .firstName("Jan")
                    .lastName("Kowalski")
                    .age(21)
                    .birthDate(LocalDate.now().minusYears(21))
                    .hasAccount(true)
                    .build();
            objectOutputStream.writeObject(person);
        }

        try (FileInputStream fileInputStream = new FileInputStream(OUTPUT_FILE);
             ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            var person = (Person) objectInputStream.readObject();
            System.out.println(person);
        }
    }

}
