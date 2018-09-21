package com.currencyfair.exercise.verticles;

import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.domain.MessageCodec;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
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
import static com.currencyfair.exercise.utils.Addresses.PUBLISH_WEB_MESSAGE_COUNTRY_TRADE_ADDRESS;

@ExtendWith(VertxExtension.class)
class CountryTradesCounterVerticleTest {

    @BeforeEach
    void setup(Vertx vertx, VertxTestContext testContext) {
        vertx.eventBus().registerDefaultCodec(Message.class, new MessageCodec());
        vertx.deployVerticle(new CountryTradesCounterVerticle(),
                // Make verticle push data every 1 second to make the test run faster
                new DeploymentOptions().setConfig(new JsonObject()
                        .put("country.trades.delay", 10)
                        .put("country.buffer.delay", 1)),
                event -> testContext.completeNow());
    }

    @Test
    void testMessagesEmptyCountryAreIgnored(Vertx vertx, VertxTestContext testContext) throws InterruptedException {

        // Fail if any message is received
        vertx.eventBus().<JsonObject>consumer(PUBLISH_WEB_MESSAGE_COUNTRY_TRADE_ADDRESS, event -> {
            // if we get a message it means that a message was pushed and not ignored
            testContext.verify(Assertions::fail);
        });

        Stream.of(null, "", " ").forEach(value ->
                vertx.eventBus().<Message>send(PUBLISH_MESSAGE_ADDRESS,
                        new Message.Builder().withOriginatingCountry(value).build()));

        testContext.awaitCompletion(2, TimeUnit.SECONDS);

        testContext.completeNow();
    }
}