package pl.training.performance.concurrency.ex19_rx_search.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QueryResult {

    private List<Repository> items;

}
