package com.currencyfair.exercise.verticles;

import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.utils.Loggable;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;

/**
 * Verticle responsible for pushing information to the frontend
 */
public class SocketPushVerticle extends AbstractVerticle implements Loggable {

    @Override
    public void start(Future<Void> startFuture) {

        // register event bus raw message address and push messages to the sockjs bridge
        this.vertx.eventBus().<Message>consumer(WebServerVerticle.PUBLISH_MESSAGE_ADDRESS)
                .bodyStream()
                .toFlowable()
                .subscribe(message -> {
                    logger().info("Received: {0}", message);
                    this.vertx.eventBus().publish(WebServerVerticle.PUBLISH_WEB_MESSAGE_ADDRESS, message.toJsonObject());
                });
    }
}
