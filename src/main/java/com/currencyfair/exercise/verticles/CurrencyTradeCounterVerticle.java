package com.currencyfair.exercise.verticles;

import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.domain.TopTraded;
import com.currencyfair.exercise.utils.Loggable;
import com.currencyfair.exercise.utils.LongCounter;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.core.AbstractVerticle;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.currencyfair.exercise.utils.Addresses.PUBLISH_MESSAGE_ADDRESS;
import static com.currencyfair.exercise.utils.Addresses.PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS;
import static com.currencyfair.exercise.utils.Addresses.REQUEST_WEB_MESSAGE_TRADED_PAIRS_ADDRESS;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Verticle responsible for maintaining a counter of messages traded by currency pair
 * Allows understanding which pair is traded more often.
 * <p>
 * To reduce load on the frontend this buffers and publishes data periodically
 * <p>
 * Only publishes the top 10 currency pairs
 */
public class CurrencyTradeCounterVerticle extends AbstractVerticle implements Loggable {

    private static final int LIMIT = 10;

    private final LongCounter longCounter = new LongCounter();

    @Override
    public void start(final Future<Void> startFuture) {

        final Long delay = this.config().getLong("trade.counter.delay", 5000L);
        final Long buffer = this.config().getLong("trade.buffer.delay", 5L);

        // register event to handle messages
        this.vertx.eventBus().<Message>consumer(PUBLISH_MESSAGE_ADDRESS)
                .bodyStream()
                .toFlowable()
                // Batch requests
                .filter(message -> currenciesDefined(message.getCurrencyFrom(), message.getCurrencyTo()))
                .buffer(buffer, TimeUnit.SECONDS, 1000)
                .subscribe(this::accept);

        // register event to handle when a new client register on the eventbus
        this.vertx.eventBus().<String>consumer(REQUEST_WEB_MESSAGE_TRADED_PAIRS_ADDRESS)
                .bodyStream()
                .toFlowable()
                .subscribe(this::requestPublish);


        // configure a event that will push traded pairs counter to the frontend every x (configurable) seconds
        this.vertx.periodicStream(delay)
                .toFlowable()
                .subscribe(this::periodicPublish);

        startFuture.complete();
    }


    private void periodicPublish(Long timer) {
        logger().trace("Timer called {0}", timer);
        publish();
    }

    private void requestPublish(String ignore) {
        publish();
    }

    /**
     * Calculates the most traded pairs and pushes the top 10 to the FE
     */
    private void publish() {
        JsonArray toSend = new JsonArray(longCounter.getEntries()
                .map((entry) -> new TopTraded.Builder().withPair(entry.getKey()).withValue(entry.getValue().longValue()))
                .map(TopTraded.Builder::build)
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .limit(LIMIT)
                .map(TopTraded::toJsonObject)
                .collect(Collectors.toList()));

        if (!toSend.isEmpty()) {
            this.vertx.eventBus().publish(PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS, toSend);
        }
    }


    /**
     * Consumes a message and counts the number of messages sent to trade a particular pair
     */
    private void accept(final List<Message> messages) {

        messages.forEach(message -> {
            // Find currency pair, and increase the counter
            String mapKey = message.getCurrencyFrom() + " / " + message.getCurrencyTo();
            longCounter.incrementByKey(mapKey);
        });
    }

    private boolean currenciesDefined(final String currencyFrom, final String currencyTo) {
        return !isNullOrEmpty(currencyFrom)
                && !isNullOrEmpty(currencyTo)
                && !currencyFrom.trim().isEmpty()
                && !currencyTo.trim().isEmpty();
    }
}
