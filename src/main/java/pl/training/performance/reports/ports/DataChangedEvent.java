package pl.training.performance.reports.ports;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.training.performance.reports.domain.DataEntry;

@Data
@AllArgsConstructor
public class DataChangedEvent {

    private DataEntry dataEntry;

}
