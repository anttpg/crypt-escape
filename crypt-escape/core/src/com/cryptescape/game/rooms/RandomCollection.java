package com.cryptescape.game.rooms;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

//Credit to ???
public class RandomCollection<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private final Random random;
    private double total = 0;

    public RandomCollection() {
        this(new Random());
    }

    public RandomCollection(Random random) {
        this.random = random;
    }
    
    public RandomCollection(double[] weight, E[] result) {
        this.random = new Random();
        
        for(int count = 0; count < weight.length; count++) {
            if (weight[count] <= 0) continue;
            total += weight[count];
            map.put(total, result[count]);
        }
    }

    public RandomCollection<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
}