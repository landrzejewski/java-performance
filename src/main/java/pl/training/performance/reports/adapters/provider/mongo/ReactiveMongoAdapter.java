package pl.training.performance.reports.adapters.provider.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.training.performance.reports.domain.DataEntry;
import pl.training.performance.reports.ports.DataChangedEvent;
import pl.training.performance.reports.ports.DataProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import static reactor.core.publisher.Mono.just;

@Repository
@RequiredArgsConstructor
public class ReactiveMongoAdapter implements DataProvider {

    private final ReactiveMongoProvider dataProvider;
    private final ReactiveMongoMappper dataMapper;

    private final Sinks.Many<DataChangedEvent> events = Sinks.many().multicast().directBestEffort();

    @Override
    public Flux<DataEntry> findAll() {
        return dataProvider.findAll()
                .map(dataMapper::toDomain);
    }

    @Override
    public Mono<DataEntry> add(DataEntry dataEntry) {
        return just(dataEntry)
                .map(dataMapper::toDocument)
                .flatMap(dataProvider::save)
                .map(dataMapper::toDomain)
                .doOnNext(this::sendDataChangeEvent);
    }

    private void sendDataChangeEvent(DataEntry dataEntry) {
        events.tryEmitNext(new DataChangedEvent(dataEntry));
    }

    @Override
    public Flux<DataChangedEvent> changeEvents() {
        return events.asFlux();
    }

}
