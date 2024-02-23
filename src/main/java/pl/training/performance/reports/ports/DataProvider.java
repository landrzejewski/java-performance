package pl.training.performance.reports.ports;

import pl.training.performance.reports.domain.DataEntry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataProvider {

    Flux<DataEntry> findAll();

    Mono<DataEntry> add(DataEntry dataEntry);

    Flux<DataChangedEvent> changeEvents();

}
