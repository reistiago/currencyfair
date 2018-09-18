package com.currencyfair.exercise;


import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.domain.MessageCodec;
import com.currencyfair.exercise.verticles.SocketPushVerticle;
import com.currencyfair.exercise.verticles.WebServerVerticle;
import io.reactivex.Completable;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.RxHelper;

import java.util.Arrays;

/**
 * Verticle responsible for the application bootstrapping
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {

        this.registerCustomCodecs();

        Completable webServer = RxHelper.deployVerticle(this.vertx, new WebServerVerticle()).toCompletable();
        Completable socketPushVerticle = RxHelper.deployVerticle(this.vertx, new SocketPushVerticle()).toCompletable();

        Completable.concat( Arrays.asList(webServer, socketPushVerticle))
                .subscribe(startFuture::complete, startFuture::fail);

    }

    private void registerCustomCodecs() {
        this.vertx.eventBus().getDelegate().registerDefaultCodec(Message.class, new MessageCodec());
    }
}
