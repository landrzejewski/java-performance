package pl.training.performance.reports.adapters.provider.mongo;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface ReactiveMongoProvider extends ReactiveMongoRepository<DataEntryDocument, Long> {

    @Query("{ 'orderDate' : { $gte: ?0, $lte: ?1 } }")
    Flux<DataEntryDocument> findByYear(LocalDate after, LocalDate before);

}
