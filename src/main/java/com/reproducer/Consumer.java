package com.reproducer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.apex.Router;
import io.vertx.ext.apex.handler.sockjs.BridgeOptions;
import io.vertx.ext.apex.handler.sockjs.PermittedOptions;
import io.vertx.ext.apex.handler.sockjs.SockJSHandler;

/**
 *
 */
public class Consumer extends AbstractVerticle {

  public void start() {
    vertx.eventBus().consumer("inbound.something", msg -> {
      vertx.executeBlocking(blocking -> {
        try {
          Thread.sleep(Main.REPLY_DELAY);
        } catch (Exception e) {
          e.printStackTrace();
        }
        blocking.complete();
      }, ar -> {
        msg.reply("[\"whatever\"]");
      });
    });

  }
}
