package com.currencyfair.exercise.utils;

/**
 * Helper class to keep a list of addresses registered on the event bus
 */
public final class Addresses {

    /**
     * Address for publishing messages for processing
     */
    public static final String PUBLISH_MESSAGE_ADDRESS = "exercise.raw-message";
    /**
     * Message to inform that a socket was open to get the traded pairs per country
     */
    public static final String REQUEST_WEB_MESSAGE_TRADED_PAIRS_ADDRESS = "request.traded.pairs.message";
    /**
     * Address to push to the FE socket in real time
     */
    public static final String PUBLISH_WEB_MESSAGE_REALTIME_ADDRESS = "realtime.messages";
    /**
     * Address to push traded pairs counter
     */
    public static final String PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS = "traded.pairs.message";
    /**
     * Address to push counter of trades per country
     */
    public static final String PUBLISH_WEB_MESSAGE_COUNTRY_TRADE_ADDRESS = "country.trade.message";

    private Addresses() {
        // no op
    }
}
