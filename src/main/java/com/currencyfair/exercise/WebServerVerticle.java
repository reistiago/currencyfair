package com.currencyfair.exercise;

import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.utils.Loggable;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;


public class WebServerVerticle extends AbstractVerticle implements Loggable {

    @Override
    public void start(Future<Void> startFuture) {


        final Router router = Router.router(this.vertx);

        this.setupStaticResourcesRoute(router);

        // Setup message ingestion route
        setupIngestionRoute(router);

        final HttpServer httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(router::accept)
                .rxListen(8080)
                .subscribe(server -> {
                    logger().info("HTTP server started");
                    startFuture.complete();
                }, startFuture::fail);
    }

    private void setupIngestionRoute(final Router router) {
        router
                .route("/ingestion")
                .method(HttpMethod.POST)
                .consumes("application/json; charset=utf-8")
                .handler(BodyHandler.create().setBodyLimit(1000))
                .handler(this::handle);
    }

    private void setupStaticResourcesRoute(final Router router) {
        router.route("/").handler(StaticHandler.create());
    }


    private void handle(io.vertx.reactivex.ext.web.RoutingContext routingContext) {
        final Message input = decodeMessage(routingContext);
        logger().info("Received: {0}", input);
        routingContext.response()
                .setStatusCode(202)
                .end();
    }

    private Message decodeMessage(final io.vertx.reactivex.ext.web.RoutingContext routingContext) {
        return Json.decodeValue(routingContext.getBodyAsString("UTF-8"), Message.class);
    }
}
