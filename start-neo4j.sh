NEO4J_PATH="../neo4j-3.3.3"
DATA_PATH="../imdb-data"

if [ ! -d "$NEO4J_PATH/data/databases/graph.db" ]; then
  $NEO4J_PATH/bin/neo4j-admin import \
    --nodes:Movie $DATA_PATH/movies.csv \
    --nodes:Actor $DATA_PATH/actors.csv \
    --relationships $DATA_PATH/roles.csv 
fi

$NEO4J_PATH/bin/neo4j console