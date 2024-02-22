package pl.training.performance.reports.adapters.provider.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.training.performance.reports.adapters.provider.fs.EagerCsvDataProvider;
import pl.training.performance.reports.domain.PageSpec;

import java.nio.file.Path;

@Transactional
@Component
@RequiredArgsConstructor
public class DataImporter implements ApplicationRunner {

    private final SpringDataMongoMappper mappper;
    private final SpringDataMongoAdapter mongoProvider;

    @Override
    public void run(ApplicationArguments args) {
        int pageSize = 10_000;
        int totalPages = (int) Math.ceil((double) 5_000_000 / pageSize);
        var provider = new EagerCsvDataProvider(Path.of("5m Sales Records.csv"));
        System.out.println("Importing...");
        for (int page = 0; page < totalPages; page++) {
            provider.findAll(new PageSpec(page, pageSize))
                    .getRows()
                    .forEach(mongoProvider::add);
            System.out.println("Page %s of %s imported".formatted(page + 1, totalPages));
        }
    }

}
