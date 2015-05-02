package com.reproducer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
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
      JsonObject jsonObj = (JsonObject) msg.body();
      final int count = jsonObj.getInteger("count");
      System.out.println("Count: "+count + " Consumer thread: " + Thread.currentThread().getId());
      
      vertx.executeBlocking(blocking -> {
        System.out.println("Count: "+count + " Blocking thread: " + Thread.currentThread().getId());
        try {
          Thread.sleep(Main.REPLY_DELAY);
        } catch (Exception e) {
          e.printStackTrace();
        }
        blocking.complete();
      }, ar -> {
        System.out.println("Count: "+count + " Reply thread: " + Thread.currentThread().getId());
        msg.reply("[\"whatever\"]");
      });
    });

  }
}
