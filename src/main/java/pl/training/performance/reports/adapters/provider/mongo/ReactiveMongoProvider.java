package pl.training.performance.reports.adapters.provider.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactiveMongoProvider extends ReactiveMongoRepository<DataEntryDocument, Long> {
}
