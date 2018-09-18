package com.currencyfair.exercise.domain;

import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MessageCodecTest {

    private MessageCodec messageCodec;

    @BeforeEach
    void setup() {
        this.messageCodec = new MessageCodec();
    }

    @Test
    void testWireEncoder() {
        final Message toSend = buildMessage();

        Buffer buffer = Buffer.buffer();
        messageCodec.encodeToWire(buffer, toSend);

        final Message received = messageCodec.decodeFromWire(0, buffer);

        assertEquals(toSend, received);
    }

    @Test
    void testInMemoryEncoding() {

        final Message toSend = buildMessage();

        assertEquals(toSend, messageCodec.transform(toSend));
    }

    private Message buildMessage() {
        return new Message.Builder()
                .withCurrencyTo("currencyTo")
                .withOriginatingCountry("country")
                .withTimePlaced("time")
                .withRate(2.0D)
                .withAmountBuy(1.0D)
                .withAmountSell(1.0D)
                .withCurrencyFrom("from")
                .withUserId("user")
                .build();
    }
}