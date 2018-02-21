package com.serli.oracle.of.bacon.repository;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.types.Path;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Neo4JRepository {
    private final Driver driver;

    public Neo4JRepository() {
        this.driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "test"));
    }

    public List<GraphItem> getConnectionsToKevinBacon(String actorName) {
        Session session = driver.session();

        final String sourceActor = "Bacon, Kevin (I)";

        // Neo4j request
        String request = "MATCH " +
            "(sourceActor:Actor {name: {sourceActor}}), " +
            "(targetActor:Actor {name: {targetActor}}), " +
            "path = shortestPath((sourceActor)-[:PLAYED_IN*]-(targetActor)) " +
            "WITH path " +
            "WHERE length(path) > 1 " +
            "RETURN path";

        Map<String, Object> params = new HashMap<>();
        params.put("sourceActor", sourceActor);
        params.put("targetActor", actorName);

        // Execute request
        StatementResult statementResult = session.run(request, params);

        // Retrieve path from result
        Optional<List<GraphItem>> path = statementResult.list().stream()
                .flatMap(record -> record.values().stream().map(Value::asPath))
                .map(this::pathToGraphItems)
                .findFirst();

        session.close();

        return path.orElse(Collections.emptyList());
    }

    private List<GraphItem> pathToGraphItems(Path path) {
        // Retrieve nodes from path and convert them to GraphNode objects
        List<GraphItem> nodes = toListConverter(
                path.nodes(),
                node -> new GraphNode(
                        node.id(),
                        node.values().iterator().next().toString(),
                        node.labels().iterator().next()
                )
        );

        // Retrieve relationships from path and convert them to GraphEdge objects
        List<GraphItem> relationships = toListConverter(
                path.relationships(),
                relationship -> new GraphEdge(
                        relationship.id(),
                        relationship.startNodeId(),
                        relationship.endNodeId(),
                        relationship.type()
                )
        );

        List<GraphItem> results = new ArrayList<>();
        results.addAll(nodes);
        results.addAll(relationships);

        return results;
    }

    private <T,R> List<R> toListConverter(Iterable<T> iterable, Function<? super T, ? extends R> converter) {
        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(converter)
                .collect(Collectors.toList());
    }

    public static abstract class GraphItem {
        public final long id;

        private GraphItem(long id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            GraphItem graphItem = (GraphItem) o;

            return id == graphItem.id;
        }

        @Override
        public int hashCode() {
            return (int) (id ^ (id >>> 32));
        }
    }

    private static class GraphNode extends GraphItem {
        public final String type;
        public final String value;

        public GraphNode(long id, String value, String type) {
            super(id);
            this.value = value;
            this.type = type;
        }
    }

    private static class GraphEdge extends GraphItem {
        public final long source;
        public final long target;
        public final String value;

        public GraphEdge(long id, long source, long target, String value) {
            super(id);
            this.source = source;
            this.target = target;
            this.value = value;
        }
    }
}
