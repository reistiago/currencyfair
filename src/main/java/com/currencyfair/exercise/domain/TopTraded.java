package com.currencyfair.exercise.domain;

import io.vertx.core.json.JsonObject;

public class TopTraded {

    private String pair;
    private Long value;

    /**
     * Ctor to be used by jackson
     */
    public TopTraded() {
        // no op
    }

    private TopTraded(Builder builder) {
        this.pair = builder.pair;
        this.value = builder.value;

    }

    public String getPair() {
        return pair;
    }

    public Long getValue() {
        return value;
    }

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("pair", pair)
                .put("value", value);
    }

    public static final class Builder {
        private String pair;
        private Long value;

        public Builder withPair(String pair) {
            this.pair = pair;
            return this;
        }

        public Builder withValue(Long value) {
            this.value = value;
            return this;
        }

        public TopTraded build() {
            return new TopTraded(this);
        }
    }
}
