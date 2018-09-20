package com.currencyfair.exercise.verticles;

import com.currencyfair.exercise.domain.Message;
import com.currencyfair.exercise.utils.Loggable;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.Json;
import io.vertx.ext.bridge.BridgeEventType;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.reactivex.ext.web.handler.StaticHandler;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;

/**
 * Verticle responsible for starting an http server. Configures routes, handlers and the event bus bridge to allow communication with the FE
 */
public class WebServerVerticle extends AbstractVerticle implements Loggable {

    static final String PUBLISH_MESSAGE_ADDRESS = "exercise.raw-message";
    static final String PUBLISH_WEB_MESSAGE_REALTIME_ADDRESS = "realtime.messages";
    static final String PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS = "traded.pairs.message";
    static final String REQUEST_WEB_MESSAGE_TRADED_PAIRS_ADDRESS = "request.traded.pairs.message";

    /**
     * Default http port, one is provided
     */
    private static final int DEFAULT_PORT = 8080;
    /**
     * Route for event bus communication
     */
    private static final String EVENT_BUS_ROUTE = "/eventbus/*";
    /**
     * Route for message ingestion
     */
    private static final String INGESTION_ROUTE = "/ingestion";
    /**
     * Max body size in bytes
     */
    private static final int MAX_BODY_SIZE = 1000;

    @Override
    public void start(Future<Void> startFuture) {

        final Router router = Router.router(this.vertx);

        this.setupStaticResourcesRoute(router);

        // Setup message ingestion route
        setupIngestionRoute(router);

        setupSockJSRoute(router);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(this.config().getInteger("http.port", DEFAULT_PORT))
                .subscribe(server -> {
                    logger().info("HTTP server started");
                    startFuture.complete();
                }, startFuture::fail);
    }

    /**
     * Setup SockJs route and handler for "real-time" communication with the FE.
     * <p>
     * Configures which addresses are exposed from the FE
     */
    private void setupSockJSRoute(Router router) {
        SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        BridgeOptions options = new BridgeOptions()
                .addOutboundPermitted(new PermittedOptions().setAddress(PUBLISH_WEB_MESSAGE_REALTIME_ADDRESS))
                .addOutboundPermitted(new PermittedOptions().setAddress(PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS));
        sockJSHandler.bridge(options, event -> {
            // send execution to the end of the context / event loop
            vertx.runOnContext(aVoid -> {
                // check if a client is connecting to request top traded pairs and send a message asking to publish the current data
                if (BridgeEventType.REGISTER.equals(event.type()) && PUBLISH_WEB_MESSAGE_TRADED_PAIRS_ADDRESS.equals(event.getRawMessage().getString("address"))) {
                    // need to send something in the message even if it is to be discarded later
                    this.vertx.eventBus().send(REQUEST_WEB_MESSAGE_TRADED_PAIRS_ADDRESS, "");
                }
            });
            event.complete(true);
        });

        router.route(EVENT_BUS_ROUTE).handler(sockJSHandler);


    }

    /**
     * Configure message ingestion route
     */
    private void setupIngestionRoute(final Router router) {
        router
                .route(INGESTION_ROUTE)
                .method(HttpMethod.POST)
                .consumes("application/json; charset=utf-8")
                .handler(BodyHandler.create().setBodyLimit(MAX_BODY_SIZE))
                .handler(this::handle);
    }

    private void setupStaticResourcesRoute(final Router router) {
        router.route("/*").handler(StaticHandler.create());
    }

    /**
     * Handles messages received by the http server for ingestion
     * <p>
     * Parses the message, publishes that message on the event bus and closes the request
     */
    private void handle(RoutingContext routingContext) {

        final Message message = decodeMessage(routingContext);

        if (message != null) {
            logger().trace("Received: {0}", message);

            this.vertx.eventBus().publish(PUBLISH_MESSAGE_ADDRESS, message);

            routingContext.response().setStatusCode(200).end();
        } else {
            // given that the message couldn't be parsed respond with bad request
            routingContext.response().setStatusCode(400).setStatusMessage("Payload could not be parsed");
        }
    }

    private Message decodeMessage(final RoutingContext routingContext) {
        try {
            return Json.decodeValue(routingContext.getBodyAsString("UTF-8"), Message.class);
        } catch (Exception ex) {
            // Given that this exception might be caused by user input we will swallow it and move forward
            return null;
        }
    }
}
