package com.example.demo.learn.design;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2024/4/22 4:05 PM
 */
public class LRUCache<K, V> {
    private final int capacity;
    private LinkedHashMap<K, V> cache;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > capacity;
            }
        };
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }

    public V get(K key) {
        return cache.getOrDefault(key, null);
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(2);
        cache.put(1, "One");
        cache.put(2, "Two");
        System.out.println(cache.get(1)); // 输出 One
        cache.put(3, "Three");
        System.out.println(cache.get(2)); // 输出 null，因为键 2 被淘汰
    }
}
