package com.currencyfair.exercise.utils;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Loggable interface for simplifying logger usage, also provides one common logger implementation for whole application.
 */
public interface Loggable {

    /**
     * Provides common, consistent logger implementation.
     *
     * @return Logger instance using this class name
     */
    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

}