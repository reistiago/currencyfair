package com.currencyfair.exercise.verticles;

import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.domain.MessageCodec;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.currencyfair.exercise.utils.Addresses.PUBLISH_MESSAGE_ADDRESS;
import static com.currencyfair.exercise.utils.Addresses.PUBLISH_WEB_MESSAGE_REALTIME_ADDRESS;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
class RealTimePushVerticleTest {

    private static final String MY_ID = "myId";

    @BeforeEach
    void setup(Vertx vertx, VertxTestContext testContext) {
        vertx.eventBus().registerDefaultCodec(Message.class, new MessageCodec());
        vertx.deployVerticle(new RealTimePushVerticle(), event -> testContext.completeNow());
    }

    @Test
    void testMessagesArePushed(Vertx vertx, VertxTestContext testContext) {
        vertx.eventBus().<JsonObject>consumer(PUBLISH_WEB_MESSAGE_REALTIME_ADDRESS, event -> {
            final String something = event.body().getString("userId");
            testContext.verify(() -> {
                assertEquals(MY_ID, something);
                testContext.completeNow();
            });
        });

        vertx.eventBus().<Message>send(PUBLISH_MESSAGE_ADDRESS,
                new Message.Builder().withUserId(MY_ID)
                        .build());
    }

}