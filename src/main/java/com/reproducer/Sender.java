package com.reproducer;

import java.util.concurrent.atomic.AtomicInteger;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

/**
 *
 */
public class Sender extends AbstractVerticle {

  public void start() {
    AtomicInteger i = new AtomicInteger(0);
    vertx.setPeriodic(
        Main.SENDER_INTERVAL,
        l -> {
          final int count = i.incrementAndGet();
          final long before = System.currentTimeMillis();
          System.out.println("Count: "+count + " Sender thread: " + Thread.currentThread().getId());
          JsonObject msg = new JsonObject();
          msg.put("count", count);
          vertx.eventBus().send(
              "inbound.something",
              msg,
              new DeliveryOptions().setSendTimeout(Main.SEND_TIMEOUT),
              ar -> {
                final long after = System.currentTimeMillis();
                final long respTime = after - before;
                if (ar.succeeded()) {
                  System.out.println("Count: "+count + " Sender: Ping-Pong completed. Response time: "
                      + respTime);
                } else {
                  System.out.println("Count: "+count + " Sender: Consumer vanished? Response time: "
                      + respTime);
                }
              });
        });
  }
}
