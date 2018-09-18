package com.currencyfair.exercise.domain;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;

public class MessageCodec implements io.vertx.core.eventbus.MessageCodec<Message, Message> {

    @Override
    public void encodeToWire(Buffer buffer, Message message) {

        JsonObject jsonToEncode = new JsonObject();
        jsonToEncode.put("userId", message.getUserId());
        jsonToEncode.put("currencyFrom", message.getCurrencyFrom());
        jsonToEncode.put("amountSell", message.getAmountSell());
        jsonToEncode.put("amountBuy", message.getAmountBuy());
        jsonToEncode.put("rate", message.getRate());
        jsonToEncode.put("timePlaced", message.getTimePlaced());
        jsonToEncode.put("originatingCountry", message.getOriginatingCountry());
        jsonToEncode.put("currencyTo", message.getCurrencyTo());

        String encoded = jsonToEncode.encode();

        int length = encoded.getBytes().length;

        buffer.appendInt(length);
        buffer.appendString(encoded);
    }

    @Override
    public Message decodeFromWire(int pos, final Buffer buffer) {
        int _pos = pos;

        int length = buffer.getInt(_pos);

        String jsonStr = buffer.getString(_pos += 4, _pos + length);
        JsonObject contentJson = new JsonObject(jsonStr);

        return new Message.Builder()
                .withUserId(contentJson.getString("userId"))
                .withCurrencyFrom(contentJson.getString("currencyFrom"))
                .withAmountSell(contentJson.getDouble("amountSell"))
                .withAmountBuy(contentJson.getDouble("amountBuy"))
                .withRate(contentJson.getDouble("rate"))
                .withTimePlaced(contentJson.getString("timePlaced"))
                .withOriginatingCountry(contentJson.getString("originatingCountry"))
                .withCurrencyTo(contentJson.getString("currencyTo"))
                .build();
    }

    @Override
    public Message transform(final Message message) {
        return message;
    }

    @Override
    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
