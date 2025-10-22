package com.kevin;

import com.kevin.enums.MessageType;
import com.kevin.models.Message;
import com.kevin.models.SubscriptionRequest;
import com.kevin.models.UnSubscriptionRequest;
import com.kevin.util.JsonUtils;
import com.kevin.ws.WebSocketConnector;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class Main {

  public static void main(String[] args) throws InterruptedException {
    WebSocketConnector connector = new WebSocketConnector();

      connector.connect("ws://localhost:8081/ws/basic");

      SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
              .channel("kevin_updates")
              .id(1L)
              .tickInterval(10000)
              .build();

      connector.send(MessageType.SUBSCRIBE, subscriptionRequest);

      Thread.sleep(3000);
      UnSubscriptionRequest unSubscriptionRequest = UnSubscriptionRequest.builder()
              .channel("kevin_updates")
              .id(1L)
              .tickInterval(10000)
              .build();

      connector.send(MessageType.UNSUBSCRIBE, unSubscriptionRequest);


      //                       {
//                                     "type": "subscribe",
//                                     "params": {
//                                       "id": 1,
//                                       "tick-interval": 10000,
//                                       "channel": "kevin_updates"
//                                     }

  }
}

