package com.backendtrack.final_;

import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.mongo.*;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;


public class Database extends AbstractVerticle {

  MongoUserUtil userUtil;

  MongoUserUtil cardUtil;

  MongoClient client;

  MongoAuthentication authentication;

  MongoAuthentication card_authentication;

  JWTAuth jwtAuth;

  EventBus bussin;

  @Override
    public void start(Promise<Void> startPromise) throws Exception {

      client = MongoClient.create(vertx, new JsonObject());

      bussin = vertx.eventBus();

      userUtil = MongoUserUtil.create(client,new MongoAuthenticationOptions(),new MongoAuthorizationOptions());

      cardUtil = MongoUserUtil.create(client,new MongoAuthenticationOptions().setCollectionName("card"),new MongoAuthorizationOptions());

      authentication = MongoAuthentication.create(client,new MongoAuthenticationOptions().setCollectionName("user"));

      card_authentication = MongoAuthentication.create(client,new MongoAuthenticationOptions().setCollectionName("card"));

      jwtAuth = JWTAuth.create(vertx, new JWTAuthOptions()
        .setKeyStore(new KeyStoreOptions()
          .setType("jceks")
          .setPath("keystore.jceks")
          .setPassword("secret")));


      bussin.<JsonObject>consumer("database", message -> {

        String action = message.body().getString("action");

        switch (action) {
          case "register":
            registerUser(message);
            break;
          case "login":
            loginUser(message);
            break;
          case "apply":
            applyCard(message);
            break;
          case "charge":
            chargeCard(message);
            break;
          case "account":
            account(message);
            break;
          case "getCard":
            getCard(message);
            break;
          case "getUser":
            getUser(message);
            break;
          default:
            message.fail(1, "Unknown action: " + message.body());
        }

      });



      startPromise.complete();
    }

    private void createUser(JsonObject user, Message<JsonObject> message) {

      userUtil.createUser(user.getString("username"),user.getString("password"), stringAsyncResult -> {

        if (stringAsyncResult.succeeded()) {
          message.reply("Successfully registered");
        } else {
          message.fail(2, "insert failed: " + stringAsyncResult.cause().getMessage());
        }

      });

    }

    private void registerUser(Message<JsonObject> message) {
      JsonObject user = message.body().getJsonObject("user");

      client.find("user", new JsonObject().put("username", user.getString("username")), listAsyncResult -> {

        if (listAsyncResult.succeeded()) {

          if (!listAsyncResult.result().isEmpty()) {

            System.out.println("User already registered");

            message.fail(1, "User already registered");

          } else {

            System.out.println("User not registered");

            createUser(user, message);

          }
        } else {
          listAsyncResult.cause().printStackTrace();
          message.fail(2, "find failed: " + listAsyncResult.cause().getMessage());
        }
      });

    }

    private void authenticateLogin(JsonObject user, Message<JsonObject> message) {

      JsonObject authInfo = new JsonObject()
        .put("username", user.getString("username"))
        .put("password", user.getString("password"));

      authentication.authenticate(authInfo, userAsyncResult -> {
        if (userAsyncResult.succeeded()) {

          message.reply(true);

        } else {
          userAsyncResult.cause().printStackTrace();
          message.fail(1, "login failed: " + userAsyncResult.cause().getMessage());
        }
      });

    }

    private void loginUser(Message<JsonObject> message) {

      JsonObject user = message.body().getJsonObject("user");

      client.find("user", new JsonObject().put("username", user.getString("username")), listAsyncResult -> {

        if (listAsyncResult.succeeded()) {
          if (!listAsyncResult.result().isEmpty()) {

            authenticateLogin(user, message);

          } else {
            message.fail(1, "User not found");
          }
        } else {
          listAsyncResult.cause().printStackTrace();
          message.fail(2, "find failed: " + listAsyncResult.cause().getMessage());
        }

      });

    }

    private void createCard(JsonObject user, String[] card, Message<JsonObject> message, AsyncResult<Message<Object>> messageAsyncResult) {

      cardUtil.createUser(user.getString("username"),card[0], stringAsyncResult -> {

        if (stringAsyncResult.succeeded()) {

          JsonObject query = new JsonObject()
            .put("username", user.getString("username"));

          JsonObject update = new JsonObject().put("$set", new JsonObject()
            .put("credit-limit", card[1])
            .put("balance", "0"));

          client.updateCollection("card", query, update, res -> {
            if (res.succeeded()) {
              message.reply(messageAsyncResult.result().body().toString());
            } else {
              message.fail(1, "update failed: " + res.cause().getMessage());
            }

          });

        } else {
          message.fail(2, "insert failed: " + stringAsyncResult.cause().getMessage());
        }

      });

    }

    private void requestCredit(JsonObject user, Message<JsonObject> message) {

      JsonObject bus_Msg = new JsonObject()
        .put("credit-score", user.getString("credit-score"));

      bussin.request("credit",bus_Msg, messageAsyncResult -> {
        if (messageAsyncResult.succeeded()) {

          String[] card = messageAsyncResult.result().body().toString().split(" ");

          createCard(user, card, message, messageAsyncResult);

        } else {
          messageAsyncResult.cause().printStackTrace();
          message.fail(1, "apply card failed: " + messageAsyncResult.cause().getMessage());
        }
      });

    }
    private void applyCard(Message<JsonObject> message) {

      JsonObject user = message.body().getJsonObject("user");

      client.find("card", new JsonObject().put("username", user.getString("username")),listAsyncResult -> {
        if (listAsyncResult.succeeded()) {
          if (!listAsyncResult.result().isEmpty()) {
            message.fail(1, "card already exists for user");
          } else {

            requestCredit(user, message);

          }
        } else {
          listAsyncResult.cause().printStackTrace();
          message.fail(2, "find failed: " + listAsyncResult.cause().getMessage());
        }
      });

    }

    private void updateCardBalance(JsonObject user, Float newBalance, Message<JsonObject> message, Float availableCredit) {

      JsonObject query = new JsonObject()
        .put("username", user.getString("username"));

      JsonObject update = new JsonObject().put("$set", new JsonObject()
        .put("balance", newBalance.toString()));

      client.updateCollection("card", query, update, res -> {
        if (res.succeeded()) {
          message.reply(newBalance.toString() + " " + availableCredit.toString());
        } else {
          message.fail(1, "update balance failed: " + res.cause().getMessage());
        }

      });

    }
    private void authenticateCharge(JsonObject user, AsyncResult<List<JsonObject>> listAsyncResult, Message<JsonObject> message) {

      JsonObject authInfo = new JsonObject()
        .put("username", user.getString("username"))
        .put("password", user.getString("password"));

      card_authentication.authenticate(authInfo, userAsyncResult -> {
        if (userAsyncResult.succeeded()) {

          JsonObject card = listAsyncResult.result().get(0);

          Float newBalance = Float.parseFloat(card.getString("balance")) + user.getFloat("amount");

          Float availableCredit = Float.parseFloat(card.getString("credit-limit")) - newBalance;

          if (newBalance < Float.parseFloat(card.getString("credit-limit"))) {

            updateCardBalance(user, newBalance, message, availableCredit);

          } else {
            message.fail(1,"Insufficient credit");
          }

        } else {
          userAsyncResult.cause().printStackTrace();
          message.fail(1, "charge failed: " + userAsyncResult.cause().getMessage());
        }
      });

    }

    private void chargeCard(Message<JsonObject> message) {

      JsonObject user = message.body().getJsonObject("user");

      client.find("card", new JsonObject().put("username", user.getString("username")), listAsyncResult -> {

        if (listAsyncResult.succeeded()) {
          if (!listAsyncResult.result().isEmpty()) {

            authenticateCharge(user, listAsyncResult, message);

          } else {
            message.fail(1, "card not found");
          }
        } else {
          listAsyncResult.cause().printStackTrace();
          message.fail(2, "find failed: " + listAsyncResult.cause().getMessage());
        }

      });

    }

    private  void getCard(Message<JsonObject> message) {

      JsonObject user = message.body().getJsonObject("user");

      client.find("card", new JsonObject().put("username", user.getString("username")), listAsyncResult -> {

        if (listAsyncResult.succeeded()) {
          if (!listAsyncResult.result().isEmpty()) {

            message.reply(listAsyncResult.result().toString());

          } else {
            message.fail(1, "card not found");
          }
        } else {
          listAsyncResult.cause().printStackTrace();
          message.fail(2, "find failed: " + listAsyncResult.cause().getMessage());
        }

      });

    }

    private void account(Message<JsonObject> message) {

      JsonObject user = message.body().getJsonObject("user");

      client.find("card", new JsonObject().put("username", user.getString("username")), listAsyncResult -> {

        if (listAsyncResult.succeeded()) {
          if (!listAsyncResult.result().isEmpty()) {

            String balance = listAsyncResult.result().get(0).getString("balance");

            String creditLimit = listAsyncResult.result().get(0).getString("credit-limit");

            message.reply("Balance: " + balance + " Credit Limit: " + creditLimit);

          } else {
            message.fail(1, "card not found");
          }
        } else {
          listAsyncResult.cause().printStackTrace();
          message.fail(2, "find failed: " + listAsyncResult.cause().getMessage());
        }

      });

    }

    private void getUser(Message<JsonObject> message) {

      JsonObject user = message.body().getJsonObject("user");

      client.find("user", new JsonObject().put("username", user.getString("username")), listAsyncResult -> {

        if (listAsyncResult.succeeded()) {
          if (!listAsyncResult.result().isEmpty()) {

            message.reply(listAsyncResult.result().get(0).toString());

          } else {
            message.fail(1, "User not found");
          }
        } else {
          listAsyncResult.cause().printStackTrace();
          message.fail(2, "find failed: " + listAsyncResult.cause().getMessage());
        }

      });

    }

}
