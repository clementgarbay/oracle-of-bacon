#!/usr/bin/env bash

# Delete the `imdb` index
curl -XDELETE 'localhost:9200/imdb'

# Create mapping of `actors` type in the `imdb` index
curl -XPUT 'localhost:9200/imdb?pretty' -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "actors": {
      "properties": {
        "suggest": {
          "type": "completion"
        },
        "name": {
          "type": "keyword"
        }
      }
    }
  }
}
'
