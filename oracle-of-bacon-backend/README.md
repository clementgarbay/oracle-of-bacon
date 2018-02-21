# Oracle of Bacon
This application is an Oracle of Bacon implementation based on NoSQL data stores :
* ElasticSearch (http) - localhost:9200
* Redis - localhost:6379
* Mongo - localhost:27017
* Neo4J (bolt) - locahost:7687

## Import data

Before using this app, you need to import data in ElasticSearch.

First, create required index by executing the `init-es.sh` script:

```
./src/main/java/com/serli/oracle/of/bacon/loader/elasticsearch/init-es.sh
```

And import data by running the *CompletionLoader* class with the path to the `actors.csv` file as argument.

## Build and run

To build :
```
./gradlew build
```

To run, execute class *com.serli.oracle.of.bacon.Application*.
