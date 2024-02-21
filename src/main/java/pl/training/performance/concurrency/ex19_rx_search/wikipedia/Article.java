package pl.training.performance.concurrency.ex19_rx_search.wikipedia;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Article {

    private String title;

}
