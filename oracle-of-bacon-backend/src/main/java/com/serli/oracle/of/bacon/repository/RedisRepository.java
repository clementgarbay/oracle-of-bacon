package com.serli.oracle.of.bacon.repository;

import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisRepository {
    private final Jedis jedis;
    private final String KEY_SEARCHES = "searches";

    public RedisRepository() {
        this.jedis = new Jedis("localhost");
    }

    public List<String> getLastTenSearches() {
        return this.jedis.lrange(KEY_SEARCHES, 0, 10);
    }

    public void addSearch(String search) {
        this.jedis.lpush(KEY_SEARCHES, search);
    }
}
