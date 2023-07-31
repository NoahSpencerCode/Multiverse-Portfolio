package com.backendtrack.final_;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;

import java.util.Objects;

public class MainVerticle extends AbstractVerticle {

  EventBus bussin;

  JWTAuth jwtAuth;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
      .setKeyStore(new KeyStoreOptions()
        .setType("jceks")
        .setPath("keystore.jceks")
        .setPassword("secret")));

    Verticle database = new Database();
    Verticle creditBureau = new CreditBureau();

    vertx.deployVerticle(creditBureau);
    vertx.deployVerticle(database);

    bussin = vertx.eventBus();

    Router router = Router.router(vertx);

    router.route("/*").handler(BodyHandler.create());

    router.post("/register").handler(this::handleRegister);
    router.post("/login").handler(this::handleLogin);
    router.post("/charge").handler(this::handleCharge);

    router.route("/actions/*").handler(JWTAuthHandler.create(jwtAuth));

    router.post("/actions/apply").handler(this::handleApply);
    router.get("/actions/account").handler(this::handleAccount);

    router.get("/actions/getUser").handler(this::handleAdminUserGet);
    router.post("/actions/postUser").handler(this::handleAdminUserPost);
    router.delete("/actions/deleteUser").handler(this::handleAdminUserDelete);

    router.get("/actions/getCard").handler(this::handleAdminCardGet);
    router.post("/actions/postCard").handler(this::handleAdminCardPost);
    router.delete("/actions/deleteCard").handler(this::handleAdminCardDelete);
    router.put("/actions/putCard").handler(this::handleAdminCardPut);

    vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private void handleRegister(RoutingContext routingContext) {
    JsonObject bus_Msg = new JsonObject()
      .put("action", "register")
      .put("user", routingContext.body().asJsonObject());

    bussin.request("database",bus_Msg, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {
        routingContext.end("Thanks for Registering!");
      } else {
        routingContext.end("Registration failed! " + messageAsyncResult.cause());
      }

    });
  }

  private void handleLogin(RoutingContext routingContext) {
    JsonObject bus_Msg = new JsonObject()
      .put("action", "login")
      .put("user", routingContext.body().asJsonObject());

    bussin.request("database",bus_Msg, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {

        String token = jwtAuth.generateToken(
          new JsonObject()
            .put("username", routingContext.body().asJsonObject().getString("username")),
          new JWTOptions().setIgnoreExpiration(true));

        routingContext.end("Welcome Back! | token = " + token);
      } else {
        routingContext.end("Login failed! " + messageAsyncResult.cause());
      }

    });
  }

  private void handleCharge(RoutingContext routingContext) {



    JsonObject bus_Msg = new JsonObject()
      .put("action", "charge")
      .put("user", routingContext.body().asJsonObject()
        .put("username", routingContext.body().asJsonObject().getString("card-holder-name"))
        .put("password", routingContext.body().asJsonObject().getString("card-number")));

    bussin.request("database",bus_Msg, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {

        String[] body = messageAsyncResult.result().body().toString().split(" ");

        routingContext.end("Card has been charged. New Balance: " + body[0] + " available credit " + body[1]);
      } else {
        routingContext.end("Card Declined! " + messageAsyncResult.cause());
      }

    });
  }

  private void handleApply(RoutingContext routingContext) {

    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    JsonObject bus_Msg = new JsonObject()
      .put("action", "apply")
      .put("user", routingContext.body().asJsonObject()
        .put("username", username));

    bussin.request("database",bus_Msg, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {

        String[] card = messageAsyncResult.result().body().toString().split(" ");

        routingContext.end("You have been approved for a credit limit of " + card[1] + " your card number is " + card[0]);
      } else {
        routingContext.end("Sorry we couldn't approve you today! " + messageAsyncResult.cause());
      }

    });
  }
  private void handleAccount(RoutingContext routingContext) {

    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    JsonObject bus_Msg = new JsonObject()
      .put("action", "account")
      .put("user", routingContext.body().asJsonObject()
        .put("username", username));

    bussin.request("database",bus_Msg, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {

        routingContext.end(messageAsyncResult.result().body().toString());
      } else {
        routingContext.end("Sorry we couldn't find your card! " + messageAsyncResult.cause());
      }

    });

  }

  private void handleAdminUserPost(RoutingContext routingContext) {

    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    if (!Objects.equals(username, "admin")) {
      routingContext.end("Not authorized");
      return;
    }

    JsonObject bus_Msg = new JsonObject()
      .put("action", "register")
      .put("user", routingContext.body().asJsonObject());

    bussin.request("database",bus_Msg, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {
        routingContext.end("User registration successful");
      } else {
        routingContext.end("Registration failed! " + messageAsyncResult.cause());
      }

    });

  }

  private void handleAdminUserGet(RoutingContext routingContext) {

    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    if (!Objects.equals(username, "admin")) {
      routingContext.end("Not authorized");
      return;
    }

    JsonObject bus_Msg = new JsonObject()
      .put("action", "getUser")
      .put("user", routingContext.body().asJsonObject());

    bussin.request("database",bus_Msg, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {
        routingContext.end(messageAsyncResult.result().body().toString());
      } else {
        routingContext.end("get failed! " + messageAsyncResult.cause());
      }

    });

  }

  private void handleAdminUserPut(RoutingContext routingContext) {
    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    if (!Objects.equals(username, "admin")) {
      routingContext.end("Not authorized");
      return;
    }


  }

  private void handleAdminCardPost(RoutingContext routingContext) {
    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    if (!Objects.equals(username, "admin")) {
      routingContext.end("Not authorized");
      return;
    }


  }

  private void handleAdminCardGet(RoutingContext routingContext) {
    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    System.out.println(username);

    if (!Objects.equals(username, "admin")) {
      routingContext.end("Not authorized");
      return;
    }

    JsonObject bus_Msg = new JsonObject()
      .put("action", "getCard")
      .put("user", routingContext.body().asJsonObject());

    bussin.request("database",bus_Msg, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {

        routingContext.end(messageAsyncResult.result().body().toString());
      } else {
        routingContext.end("Could not get card " + messageAsyncResult.cause());
      }

    });
  }

  private void handleAdminCardPut(RoutingContext routingContext) {

    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    if (!Objects.equals(username, "admin")) {
      routingContext.end("Not authorized");
      return;
    }

  }

  private void handleAdminCardDelete(RoutingContext routingContext) {

    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    if (!Objects.equals(username, "admin")) {
      routingContext.end("Not authorized");
      return;
    }

  }

  private void handleAdminUserDelete(RoutingContext routingContext) {

    User jwtUser = routingContext.user();

    JsonObject prince = jwtUser.principal();

    String username = prince.getString("username");

    if (!Objects.equals(username, "admin")) {
      routingContext.end("Not authorized");
      return;
    }

  }

}
