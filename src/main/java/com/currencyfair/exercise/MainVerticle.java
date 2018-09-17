package com.currencyfair.exercise;


import io.reactivex.Single;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.RxHelper;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) {
        Single<String> webServer = RxHelper.deployVerticle(this.vertx, new WebServerVerticle());

        webServer.subscribe(id -> startFuture.complete(), startFuture::fail);
    }
}
