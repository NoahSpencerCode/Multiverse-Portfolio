package com.backendtrack.final_;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.util.Locale;
import java.util.Random;

public class CreditBureau extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    EventBus bussin = vertx.eventBus();

    bussin.<JsonObject>consumer("credit", message -> {

      Double credit = Double.parseDouble(message.body().getString("credit-score"));

      System.out.println("Credit Bureau: " + credit);

      credit = Math.pow(credit / 100, 4);

      String credit_Send = credit.toString();

      Random rand = new Random();
      String cardNumber = String.format((Locale) null,
        "%04d-%04d-%04d-%04d",
        rand.nextInt(1000),
        rand.nextInt(1000),
        rand.nextInt(1000),
        rand.nextInt(1000));

      message.reply(cardNumber + " " + credit_Send);

    });

    startPromise.complete();

  }
}
