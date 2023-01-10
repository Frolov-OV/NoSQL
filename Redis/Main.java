package org.example;

import redis.clients.jedis.Jedis;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Jedis client = new Jedis("localhost", 6379);
        Website website = new Website(client);
        website.start();
    }
}
