package com.thm;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.LinkedHashSet;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * Hello world!
 */
/*
Chinh sua tren client
*/
public class App {
    public static void main(String[] args) {
        LoadingCache<Integer, LinkedHashSet<Integer>> primeCache =
                CacheBuilder.newBuilder()
                        .expireAfterWrite(20, TimeUnit.SECONDS)
                        .expireAfterAccess(10, TimeUnit.SECONDS)
                        .build(new CacheLoader<Integer, LinkedHashSet<Integer>>() {
                            @Override
                            public LinkedHashSet<Integer> load(Integer key) {
                                return getPrimeSet(key);
                            }
                        });
        port(8080);
        get("/prime", (req, res) -> {
            int n = Integer.parseInt(req.queryParams("n"));
            LinkedHashSet<Integer> response = primeCache.get(n);
            return response.toString();
        });
    }

    private static LinkedHashSet<Integer> getPrimeSet(int n) {
        LinkedHashSet<Integer> resultSet = new LinkedHashSet<>();
        IntStream.rangeClosed(2, n).forEach((i) -> {
            if (isPrime(i)) {
                resultSet.add(i);
            }
        });
        return resultSet;
    }

    private static boolean isPrime(int n) {
        boolean result = true;
        int i;
        for (i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                result = false;
            }
        }
        return result;
    }
}
