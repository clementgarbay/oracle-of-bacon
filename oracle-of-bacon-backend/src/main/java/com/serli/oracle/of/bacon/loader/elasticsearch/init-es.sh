#!/usr/bin/env bash

curl -XDELETE 'localhost:9200/imdb'

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
