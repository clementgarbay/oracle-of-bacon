package com.serli.oracle.of.bacon.loader.elasticsearch;

import com.serli.oracle.of.bacon.repository.ElasticSearchRepository;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.serli.oracle.of.bacon.repository.ElasticSearchRepository.ES_INDEX;
import static com.serli.oracle.of.bacon.repository.ElasticSearchRepository.ES_TYPE;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class CompletionLoader {
    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws IOException, InterruptedException {
        RestHighLevelClient client = ElasticSearchRepository.createClient();

        if (args.length != 1) {
            System.err.println("Expecting 1 arguments, actual : " + args.length);
            System.err.println("Usage : completion-loader <actors file path>");
            System.exit(-1);
        }

        String inputFilePath = args[0];

        // Create bulk processor
        BulkProcessor bulkProcessor = buildBulkProcessor(client);

        try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(inputFilePath))) {
            List<String> actorsNames = bufferedReader.lines().collect(Collectors.toList());

            // Create an IndexRequest for each actor, and add it to the bulk processor
            for (String actorName : actorsNames) {
                String actorNameWithoutQuotes = actorName.substring(1, actorName.length() - 1);
                IndexRequest indexRequest = toIndexRequestOfSuggestDoc(ES_INDEX, ES_TYPE, actorNameWithoutQuotes);

                count.incrementAndGet();
                bulkProcessor.add(indexRequest);
            }
        }

        System.out.println("Inserted total of " + count.get() + " actors");

        bulkProcessor.flush();
        bulkProcessor.close();
        
        client.close();
    }

    /**
     * Create a bulk processor for import to process IndexRequest by flushing every 8MB
     */
    private static BulkProcessor buildBulkProcessor(RestHighLevelClient client) {
        return BulkProcessor.builder(client::bulkAsync, new BulkProcessor.Listener() {
            @Override
            public void beforeBulk(long executionId, BulkRequest request) {
                System.out.println("Start bulk " + executionId);
            }
            @Override
            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                System.out.println("Bulk " + executionId + " succeeded");
            }
            @Override
            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                System.out.println("Bulk " + executionId + " failed: " + failure.getMessage());
            }
        }).setBulkActions(-1).setBulkSize(new ByteSizeValue(8, ByteSizeUnit.MB)).build();
    }

    /**
     * Create an IndexRequest of a basic document with suggestion for completion
     */
    private static IndexRequest toIndexRequestOfSuggestDoc(String esIndex, String esType, String name) throws IOException {
        return new IndexRequest(esIndex, esType)
            .source(
                jsonBuilder()
                    .startObject()
                        .field("name", name)
                        .field("suggest", generateSuggest(name))
                    .endObject()
            );
    }

    private static List<String> generateSuggest(String str) {
        List<String> suggestions = new ArrayList<>();
        List<String> strParts = Arrays.asList(str.split(" "));

        suggestions.add(str);
        suggestions.addAll(strParts);

        return suggestions;
    }
}
