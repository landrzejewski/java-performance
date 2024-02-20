package pl.training.performance.serialization;

import java.io.*;
import java.time.LocalDate;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SerializationTest {

    private static final String OUTPUT_FILE = "person.ser";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(OUTPUT_FILE);
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream)) {
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
             GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(gzipInputStream)) {
            var person = (Person) objectInputStream.readObject();
            System.out.println(person);
        }

        // Standard Serializable  217 B
        // Custom Externalizable  127
    }

}
