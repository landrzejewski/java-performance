package pl.training.performance.concurrency.ex14_phaser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.function.Predicate;

public class Application {

    public static void main(String[] args) {
        Phaser phaser = new Phaser(3);
        Path path = Paths.get(".");
        Predicate<File> filePredicate = file -> file.length() > 1024;
        List.of(
                new Search(path, "java", filePredicate, phaser),
                new Search(path, "txt", filePredicate, phaser),
                new Search(path, "class", filePredicate, phaser)
        ).forEach(searchFiles -> new Thread(searchFiles).start());
    }

}
