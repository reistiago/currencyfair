package com.currencyfair.exercise.domain;

import io.vertx.core.json.JsonObject;

public class CountryTraded {
    private String country;
    private Long counter;

    /**
     * Ctor to be used by jackson
     */
    public CountryTraded() {
        // no op
    }

    private CountryTraded(CountryTraded.Builder builder) {
        this.country = builder.country;
        this.counter = builder.counter;

    }

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("country", country)
                .put("counter", counter);
    }

    public static final class Builder {
        private String country;
        private Long counter;

        public CountryTraded.Builder withCountry(final String country) {
            this.country = country;
            return this;
        }

        public CountryTraded.Builder withCounter(final Long counter) {
            this.counter = counter;
            return this;
        }

        public CountryTraded build() {
            return new CountryTraded(this);
        }
    }
}
