package com.reproducer;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class Main {

  static boolean showBug = true;

  public static final Long SENDER_INTERVAL = 1000l;
  public static final Long REPLY_DELAY = showBug ? SENDER_INTERVAL + 500l : SENDER_INTERVAL - 500l;
  public static final Long SEND_TIMEOUT = REPLY_DELAY * 2;

  public static void main(String[] args) {
    // Normal mode
     Vertx vertx = Vertx.vertx();
     deployVerticles(vertx);

    // Cluster mode
//    VertxOptions options = new VertxOptions();
//    options.setClustered(true);
//    options.setClusterManager(new HazelcastClusterManager());
//    Vertx.clusteredVertx(options, vertxHandler -> {
//      if (vertxHandler.succeeded()) {
//        Vertx vertx = vertxHandler.result();
//        deployVerticles(vertx);
//      }
//    });
  }

  private static void deployVerticles(Vertx vertx) {
    vertx.deployVerticle(new Sender());
    DeploymentOptions options = new DeploymentOptions();
    options.setInstances(1);
    vertx.deployVerticle(new Consumer(), options);
  }
}
