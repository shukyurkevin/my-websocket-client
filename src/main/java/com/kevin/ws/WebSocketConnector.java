package com.kevin.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WebSocketConnector {
  private final OkHttpClient client;

  private WebSocket webSocket;
    public WebSocketConnector(){
    this.client = new OkHttpClient();
  }

  public void connect(String url){
    Request request = new Request.Builder()
        .url(url)
        .build();

      webSocket = client.newWebSocket(request, new WebSocketListener() {

          @Override
          public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
              super.onClosed(webSocket, code, reason);
          }

          @Override
          public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
              super.onClosing(webSocket, code, reason);
          }

          @Override
          public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t,
                                @Nullable Response response) {
              super.onFailure(webSocket, t, response);
          }

          @Override
          public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
              System.out.println(text);
              super.onMessage(webSocket, text);
          }

          @Override
          public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
              super.onMessage(webSocket, bytes);
          }

          @Override
          public void onOpen(@NotNull WebSocket webSocket, Response response) {
              System.out.println("Connected, sending subscription...");

              String subscribeMsg = """
                       {
                                     "type": "subscribe",
                                     "params": {
                                       "id": 1,
                                       "tick-interval": 100,
                                       "channel": "kevin_updates"
                                     }
                                   }
                      """;
              webSocket.send(subscribeMsg);
              System.out.println("Subscription subscribed!");
          }

      });

  }

}
