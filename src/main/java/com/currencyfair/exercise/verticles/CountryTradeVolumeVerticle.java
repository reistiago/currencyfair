package com.currencyfair.exercise.verticles;

import com.currencyfair.exercise.domain.CountryTraded;
import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.utils.Loggable;
import com.currencyfair.exercise.utils.LongCounter;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.core.AbstractVerticle;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.currencyfair.exercise.verticles.WebServerVerticle.PUBLISH_WEB_MESSAGE_COUNTRY_TRADE_ADDRESS;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.util.Objects.nonNull;

/**
 * Verticle responsible for calculating the number of trades per country
 *
 * To reduce load on the frontend this buffers and publishes data periodically
 */
public class CountryTradeVolumeVerticle extends AbstractVerticle implements Loggable {

    private final LongCounter counter = new LongCounter();

    @Override
    public void start(final Future<Void> startFuture) {

        final Long delay = this.config().getLong("country.trades.delay", 5000L);
        final Long buffer = this.config().getLong("country.buffer.delay", 5L);

        // register event to handle messages
        this.vertx.eventBus().<Message>consumer(WebServerVerticle.PUBLISH_MESSAGE_ADDRESS)
                .bodyStream()
                .toFlowable()
                // Batch requests
                .filter(message -> !isNullOrEmpty(message.getOriginatingCountry()) && !message.getOriginatingCountry().trim().isEmpty())
                .filter(message -> nonNull(message.getAmountBuy()))
                .buffer(buffer, TimeUnit.SECONDS, 1000)
                .subscribe(this::accept);

        // configure a event that country trades
        this.vertx.periodicStream(delay)
                .toFlowable()
                .subscribe(this::periodicPublish);

        startFuture.complete();
    }


    private void periodicPublish(Long timer) {
        logger().trace("Timer called {0}", timer);
        publish();
    }

    /**
     * Calculates the most traded pairs and pushes the top 10 to the FE
     */
    private void publish() {
        JsonArray toSend = new JsonArray(counter.getEntries()
                .map((entry) -> new CountryTraded.Builder().withCountry(entry.getKey()).withCounter(entry.getValue().longValue()))
                .map(CountryTraded.Builder::build)
                .map(CountryTraded::toJsonObject)
                .collect(Collectors.toList()));

        if (!toSend.isEmpty()) {
            this.vertx.eventBus().publish(PUBLISH_WEB_MESSAGE_COUNTRY_TRADE_ADDRESS, toSend);
        }
    }

    /**
     * Consumes a message and counts the number of messages sent to trade a particular pair
     */
    private void accept(final List<Message> messages) {
        messages.forEach(message -> {
            final String mapKey = message.getOriginatingCountry().trim().toUpperCase();
            counter.incrementByKey(mapKey);
        });
    }
}
