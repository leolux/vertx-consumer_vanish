package com.reproducer;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;

/**
 *
 */
public class Sender extends AbstractVerticle {

  public void start() {
    vertx.setPeriodic(Main.SENDER_INTERVAL, l -> {
      DeliveryOptions options = new DeliveryOptions();
      options.setSendTimeout(Main.SENDER_INTERVAL);
      vertx.eventBus().send("inbound.something", "{\"data\": \"Hello\"}", options, ar -> {
        if(ar.succeeded()){
          System.out.println("Sender: Ping-Pong completed");
        }else{
          System.out.println("Sender: Consumer vanished?");
        }
      });
    });
  }
}
