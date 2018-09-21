package com.currencyfair.exercise.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Stream;

public class LongCounter {

    private final Map<String, LongAdder> counter = new HashMap<>();

    public void incrementByKey(String mapKey) {
        counter.compute(mapKey, (key, value) -> {
            if (value == null) {
                value = new LongAdder();
            }
            value.increment();
            return value;
        });
    }

    public Stream<Map.Entry<String, LongAdder>> getEntries() {
        return counter.entrySet().stream();
    }



}
