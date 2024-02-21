package pl.training.performance.concurrency.ex19_rx_search.wikipedia;

import io.reactivex.Observable;
import retrofit2.Retrofit;

import java.util.List;

public class WikipediaService {

    private final WikipediaApi wikipediaApi;

    public WikipediaService(Retrofit retrofit) {
        wikipediaApi = retrofit.create(WikipediaApi.class);
    }

    public Observable<List<Article>> getArticles(String query) {
        return wikipediaApi.getArticles(query)
                .map(response -> response.getQuery().getSearch());
    }

}
