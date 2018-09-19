package com.currencyfair.exercise.verticles;

import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.domain.TopTraded;
import com.currencyfair.exercise.utils.Loggable;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.core.AbstractVerticle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

import static com.currencyfair.exercise.verticles.WebServerVerticle.PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Verticle responsible for maintaining a counter of messages traded by currency pair
 * Allows understanding which pair is traded more often.
 * <p>
 * Only publishes the top 10 currency pairs
 */
public class CurrencyTradeCounterVerticle extends AbstractVerticle implements Loggable {

    private static final int LIMIT = 10;

    private Map<String, LongAdder> currencyMap = new HashMap<>();

    @Override
    public void start(Future<Void> startFuture) {

        Long delay = this.config().getLong("trade.counter.delay", 10000L);

        // register event to handle messages
        this.vertx.eventBus().<Message>consumer(WebServerVerticle.PUBLISH_MESSAGE_ADDRESS)
                .bodyStream()
                .toFlowable()
                // Batch requests
                .filter( message -> currenciesDefined(message.getCurrencyFrom(), message.getCurrencyTo()))
                .buffer(5, TimeUnit.SECONDS, 1000)
                .subscribe(this::accept);

        this.vertx.eventBus().<String>consumer(WebServerVerticle.REQUEST_WEB_MESSAGE_TRADED_PAIRS_ADDRESS)
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
        JsonArray toSend = new JsonArray(currencyMap.entrySet()
                .stream()
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

          currencyMap.compute(mapKey, (key, value) -> {
              if (value == null) {
                  value = new LongAdder();
              }
              value.increment();
              return value;
          });
      });
    }

    private boolean currenciesDefined(final String currencyFrom, final String currencyTo) {
        return !isNullOrEmpty(currencyFrom)
                && !isNullOrEmpty(currencyTo)
                && !currencyFrom.trim().isEmpty()
                && !currencyTo.trim().isEmpty();
    }
}
