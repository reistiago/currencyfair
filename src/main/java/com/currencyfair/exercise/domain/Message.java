package com.currencyfair.exercise.domain;

import java.util.Objects;

public class Message {

    private String userId;
    private String currencyFrom;
    private double amountSell;
    private double amountBuy;
    private double rate;
    private String timePlaced;
    private String originatingCountry;
    private String currencyTo;


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

    public double getAmountSell() {
        return amountSell;
    }

    public void setAmountSell(double amountSell) {
        this.amountSell = amountSell;
    }

    public double getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(double amountBuy) {
        this.amountBuy = amountBuy;
    }

    public double getRate() {
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
}
