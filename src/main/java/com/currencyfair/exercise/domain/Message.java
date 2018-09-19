package com.currencyfair.exercise.domain;

import io.vertx.core.json.JsonObject;

import java.util.Objects;

public class Message {

    private String userId;
    private String currencyFrom;
    private Double amountSell;
    private Double amountBuy;
    private Double rate;
    private String timePlaced;
    private String originatingCountry;
    private String currencyTo;

    /**
     * Ctor to be used by jackson
     */
    public Message() {
        // no op
    }

    private Message(Builder builder) {
        this.userId = builder.userId;
        this.currencyFrom = builder.currencyFrom;
        this.amountSell = builder.amountSell;
        this.amountBuy = builder.amountBuy;
        this.rate = builder.rate;
        this.timePlaced = builder.timePlaced;
        this.originatingCountry = builder.originatingCountry;
        this.currencyTo = builder.currencyTo;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public Double getAmountSell() {
        return amountSell;
    }

    public void setAmountSell(double amountSell) {
        this.amountSell = amountSell;
    }

    public Double getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(double amountBuy) {
        this.amountBuy = amountBuy;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(String timePlaced) {
        this.timePlaced = timePlaced;
    }

    public String getOriginatingCountry() {
        return originatingCountry;
    }

    public void setOriginatingCountry(String originatingCountry) {
        this.originatingCountry = originatingCountry;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return Double.compare(message.amountSell, amountSell) == 0 &&
                Double.compare(message.amountBuy, amountBuy) == 0 &&
                Double.compare(message.rate, rate) == 0 &&
                Objects.equals(userId, message.userId) &&
                Objects.equals(currencyFrom, message.currencyFrom) &&
                Objects.equals(timePlaced, message.timePlaced) &&
                Objects.equals(originatingCountry, message.originatingCountry) &&
                Objects.equals(currencyTo, message.currencyTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, currencyFrom, amountSell, amountBuy, rate, timePlaced, originatingCountry, currencyTo);
    }

    @Override
    public String toString() {
        return "Message{" + "userId='" + userId + '\'' +
                ", currencyFrom='" + currencyFrom + '\'' +
                ", amountSell=" + amountSell +
                ", amountBuy=" + amountBuy +
                ", rate=" + rate +
                ", timePlaced='" + timePlaced + '\'' +
                ", originatingCountry='" + originatingCountry + '\'' +
                ", currencyTo='" + currencyTo + '\'' +
                '}';
    }

    public JsonObject toJsonObject() {
        return new JsonObject()
                .put("userId", userId)
                .put("currencyFrom", currencyFrom)
                .put("amountSell", amountSell)
                .put("amountBuy", amountBuy)
                .put("rate", rate)
                .put("timePlaced", timePlaced)
                .put("originatingCountry", originatingCountry)
                .put("currencyTo", currencyTo);

    }

    public static final class Builder {
        private String userId;
        private String currencyFrom;
        private Double amountSell;
        private Double amountBuy;
        private Double rate;
        private String timePlaced;
        private String originatingCountry;
        private String currencyTo;

        public Builder withUserId(final String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withCurrencyFrom(final String currencyFrom) {
            this.currencyFrom = currencyFrom;
            return this;
        }

        public Builder withAmountSell(final Double amountSell) {
            this.amountSell = amountSell;
            return this;
        }

        public Builder withAmountBuy(final Double amountBuy) {
            this.amountBuy = amountBuy;
            return this;
        }

        public Builder withRate(final Double rate) {
            this.rate = rate;
            return this;
        }

        public Builder withTimePlaced(final String timePlaced) {
            this.timePlaced = timePlaced;
            return this;
        }

        public Builder withOriginatingCountry(final String originatingCountry) {
            this.originatingCountry = originatingCountry;
            return this;
        }

        public Builder withCurrencyTo(final String currencyTo) {
            this.currencyTo = currencyTo;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
