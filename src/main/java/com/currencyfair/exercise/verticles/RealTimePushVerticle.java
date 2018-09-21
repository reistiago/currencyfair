package com.currencyfair.exercise.verticles;

import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.utils.Loggable;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;

import static com.currencyfair.exercise.utils.Addresses.PUBLISH_MESSAGE_ADDRESS;
import static com.currencyfair.exercise.utils.Addresses.PUBLISH_WEB_MESSAGE_REALTIME_ADDRESS;

/**
 * Verticle responsible for pushing information to the frontend
 */
public class RealTimePushVerticle extends AbstractVerticle implements Loggable {

    @Override
    public void start(Future<Void> startFuture) {

        // register event bus raw message address and push messages to the sockjs bridge
        this.vertx.eventBus().<Message>consumer(PUBLISH_MESSAGE_ADDRESS)
                .bodyStream()
                .toFlowable()
                .subscribe(message -> {
                    logger().trace("Received: {0}", message);
                    this.vertx.eventBus().publish(PUBLISH_WEB_MESSAGE_REALTIME_ADDRESS, message.toJsonObject());
                });

        startFuture.complete();
    }
}
