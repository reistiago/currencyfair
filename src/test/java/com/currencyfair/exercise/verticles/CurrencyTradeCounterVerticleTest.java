package com.currencyfair.exercise.verticles;

import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.domain.MessageCodec;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.currencyfair.exercise.utils.Addresses.PUBLISH_MESSAGE_ADDRESS;
import static com.currencyfair.exercise.utils.Addresses.PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
class CurrencyTradeCounterVerticleTest {

    @BeforeEach
    void setup(Vertx vertx, VertxTestContext testContext) {
        vertx.eventBus().registerDefaultCodec(Message.class, new MessageCodec());
        vertx.deployVerticle(new CurrencyTradeCounterVerticle(),
                new DeploymentOptions().setConfig(new JsonObject()
                        .put("trade.counter.delay", 10)
                        .put("trade.buffer.delay", 1)),
                event -> testContext.completeNow());
    }

    @Test
    void testMessagesEmptyCurrenciesAreIgnored(Vertx vertx, VertxTestContext testContext) throws InterruptedException {

        // Fail if any message is received
        vertx.eventBus().<JsonObject>consumer(PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS, event -> {
            // if we get a message it means that a message was pushed and not ignored
            testContext.verify(Assertions::fail);
        });

        Stream.of(null, "", "from").forEach(value -> vertx.eventBus().<Message>send(PUBLISH_MESSAGE_ADDRESS,
                new Message.Builder().withCurrencyFrom(value).build()));

        Stream.of("", " ").forEach(value -> vertx.eventBus().<Message>send(PUBLISH_MESSAGE_ADDRESS,
                new Message.Builder().withCurrencyFrom("from").withCurrencyTo(value).build()));

        testContext.awaitCompletion(2, TimeUnit.SECONDS);

        testContext.completeNow();
    }

    @Test
    void testMessagesAreGrouped(Vertx vertx, VertxTestContext testContext) {

        vertx.eventBus().<JsonArray>consumer(PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS, event -> {

            JsonArray content = event.body();
            testContext.verify(() -> assertFalse(content.isEmpty()));
            testContext.verify(() -> assertEquals("gb / eur", content.stream().findFirst()
                    .map(JsonObject.class::cast)
                    .map(json -> json.getString("pair"))
                    .orElse("Nothing was found on the array")));

            testContext.verify(() -> assertTrue(content.stream().findFirst()
                    .map(JsonObject.class::cast)
                    .map(json -> json.getLong("value"))
                    .map(value -> value > 1)
                    .orElse(false)));

            testContext.completeNow();
        });


        Stream.of(1.0D, 2.0D, 3.0D, 4.0, 5.0).forEach(value -> {
            vertx.eventBus().<Message>send(PUBLISH_MESSAGE_ADDRESS,
                    new Message.Builder().withCurrencyTo("eur").withCurrencyFrom("gb").withAmountBuy(value).build());
        });
    }
}