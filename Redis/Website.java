package org.example;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.resps.ScanResult;
import redis.clients.jedis.resps.Tuple;

import java.util.Date;
import java.util.List;

public class Website {
    private final Jedis client;
    List<Tuple> result;

    public Website(Jedis client) {
        this.client = client;
    }

    public void start() throws InterruptedException {
        newClient();
        System.out.println("Первые 20 клиентов: ");
        while (true) {
            print();
            Thread.sleep(1000);
            System.out.println("\n Следующие 20 клиентов:");
            Thread.sleep(1000);
        }


    }

    private void newClient() {
        removeKey();
        for (int i = 1; i <= 20; i++) {
            client.zadd("Users", new Date().getTime(), String.valueOf(i));
        }
        ScanResult<Tuple> users = client.zscan("Users", "0");
        result = users.getResult();
    }

    private void print() {
        for (Tuple user : result) {
            System.out.println("— На главной странице показываем пользователя " + user.getElement());
            try {
                Thread.sleep(1000);
                if (Math.random() < 0.10) {
                    subscription();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void subscription() throws InterruptedException {
        int i = (int) (0 + result.size() * Math.random());
        String element = result.get(i).getElement();
        System.out.println("> Пользователь " + element + " оплатил платную услугу");
        System.out.println("— На главной странице показываем пользователя " + element);
        Thread.sleep(1000);

    }

    private void removeKey() {
        client.del("Users");
    }

}
