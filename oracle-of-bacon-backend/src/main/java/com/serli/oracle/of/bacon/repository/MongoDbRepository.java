package com.serli.oracle.of.bacon.repository;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.Optional;

public class MongoDbRepository {
    private final MongoCollection<Document> actorCollection;

    private static final String DATABASE = "workshop";
    private static final String COLLECTION = "actors";

    public MongoDbRepository() {
        this.actorCollection = new MongoClient("localhost", 27017)
                .getDatabase(DATABASE)
                .getCollection(COLLECTION);
    }

    public Optional<Document> getActorByName(String name) {
        Document actor = this.actorCollection.find(Filters.eq("name", name)).first();
        return Optional.ofNullable(actor);
    }
}
