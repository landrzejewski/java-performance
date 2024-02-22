package pl.training.performance.reports.adapters.provider.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpringDataMongoProvider extends MongoRepository<DataEntryDocument, Long> {
}
