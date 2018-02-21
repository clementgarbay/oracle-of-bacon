package com.serli.oracle.of.bacon.repository;

import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ElasticSearchRepository {

    private final RestHighLevelClient client;

    public static final String ES_INDEX = "imdb";
    public static final String ES_TYPE = "actors";

    public ElasticSearchRepository() {
        client = createClient();
    }

    public static RestHighLevelClient createClient() {
        return new RestHighLevelClient(
            RestClient.builder(
                new HttpHost("localhost", 9200, "http")
            )
        );
    }

    public List<String> getActorsSuggests(String searchQuery) throws IOException {
        final String SUGGESTION_NAME = "actor-name-suggest";

        // Build search request with a completion suggestion
        SuggestionBuilder suggestionBuilder = SuggestBuilders.completionSuggestion("suggest").prefix(searchQuery, Fuzziness.AUTO);
        SuggestBuilder suggestBuilder = new SuggestBuilder().addSuggestion(SUGGESTION_NAME, suggestionBuilder);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().suggest(suggestBuilder);
        SearchRequest searchRequest = new SearchRequest(ES_INDEX).types(ES_TYPE).source(searchSourceBuilder);

        // Execute request
        SearchResponse searchResponse = client.search(searchRequest);

        // Retrieve suggestions actor's names from `suggest` field
        CompletionSuggestion completionSuggestion = searchResponse.getSuggest().getSuggestion(SUGGESTION_NAME);

        return completionSuggestion
                .getEntries().stream()
                .flatMap(entry -> entry.getOptions().stream())
                .map(options -> options.getHit().getSourceAsMap().get("name").toString())
                .collect(Collectors.toList());
    }
}
