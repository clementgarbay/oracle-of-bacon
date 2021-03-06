package com.serli.oracle.of.bacon.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MockedResponses {

    public static String getConnectionsToKevinBacon(String actorName) {
        return "[\n" +
                "{\n" +
                "\"data\": {\n" +
                "\"id\": 85449,\n" +
                "\"type\": \"Actor\",\n" +
                "\"value\": \"Bacon, Kevin (I)\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"data\": {\n" +
                "\"id\": 2278636,\n" +
                "\"type\": \"Movie\",\n" +
                "\"value\": \"Mystic River (2003)\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"data\": {\n" +
                "\"id\": 1394181,\n" +
                "\"type\": \"Actor\",\n" +
                "\"value\": \"Robbins, Tim (I)\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"data\": {\n" +
                "\"id\": 579848,\n" +
                "\"source\": 85449,\n" +
                "\"target\": 2278636,\n" +
                "\"value\": \"PLAYED_IN\"\n" +
                "}\n" +
                "},\n" +
                "{\n" +
                "\"data\": {\n" +
                "\"id\": 9985692,\n" +
                "\"source\": 1394181,\n" +
                "\"target\": 2278636,\n" +
                "\"value\": \"PLAYED_IN\"\n" +
                "}\n" +
                "}\n" +
                "]";
    }

    public static List<String> getActorSuggestion(String searchQuery) throws IOException {
        return Arrays.asList("Niro, Chel",
                "Senanayake, Niro",
                "Niro, Juan Carlos",
                "de la Rua, Niro",
                "Niro, Simão");
    }

    public static List<String> last10Searches() {
        return Arrays.asList("Peckinpah, Sam",
                "Robbins, Tim (I)",
                "Freeman, Morgan (I)",
                "De Niro, Robert",
                "Pacino, Al (I)");
    }

    public static String getActorByName(String actorName) {
        return "{\n" +
                "\"_id\": {\n" +
                "\"$oid\": \"587bd993da2444c943a25161\"\n" +
                "},\n" +
                "\"imdb_id\": \"nm0000134\",\n" +
                "\"name\": \"Robert De Niro\",\n" +
                "\"birth_date\": \"1943-08-17\",\n" +
                "\"description\": \"Robert De Niro, thought of as one of the greatest actors of all time, was born in Greenwich Village, Manhattan, New York City, to artists Virginia (Admiral) and Robert De Niro Sr. His paternal grandfather was of Italian descent, and his other ancestry is Irish, German, Dutch, English, and French. He was trained at the Stella Adler Conservatory and...\",\n" +
                "\"image\": \"https://images-na.ssl-images-amazon.com/images/M/MV5BMjAwNDU3MzcyOV5BMl5BanBnXkFtZTcwMjc0MTIxMw@@._V1_UY317_CR13,0,214,317_AL_.jpg\",\n" +
                "\"occupation\": [\n" +
                "\"actor\",\n" +
                "\"producer\",\n" +
                "\"soundtrack\"\n" +
                "]\n" +
                "}";
    }
}
