package com.currencyfair.exercise.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LongCounterTest {

    private LongCounter longCounter;

    @BeforeEach
    void setup() {
        longCounter = new LongCounter();
    }

    @Test
    void testEmptyNoResults() {
        assertTrue(longCounter.getEntries().collect(toList()).isEmpty());
    }


    @Test
    void testSingleEntryIncrements() {
        longCounter.incrementByKey("key");
        longCounter.incrementByKey("key");
        longCounter.incrementByKey("key");
        longCounter.incrementByKey("key");

        List<Map.Entry<String, LongAdder>> found = longCounter.getEntries().collect(toList());
        assertEquals(1, found.size());

        Long counter = found.stream().findFirst().map(Map.Entry::getValue).map(LongAdder::longValue).orElse(null);

        assertEquals(Long.valueOf(4), counter);
    }
}