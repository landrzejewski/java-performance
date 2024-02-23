package pl.training.performance.reports.adapters.provider.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pl.training.performance.reports.adapters.provider.mongo.DataEntryDocument;

public interface ReactiveSpringDataMongoProvider extends ReactiveMongoRepository<DataEntryDocument, Long> {
}
