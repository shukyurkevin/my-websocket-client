package com.kevin.ws;

import com.kevin.enums.MessageType;
import com.kevin.models.Message;
import com.kevin.util.JsonUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketConnector {
    private final OkHttpClient client;
    private volatile WebSocket webSocket;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final int MAX_RECONECT_ATTEMPTS = 3;
    private final long RECONNECT_DELAY_MS = 2000;
    private final long MAX_DELAY_MS = 30000;
    private final AtomicInteger reconnectAttemps = new AtomicInteger(0);
    private final CountDownLatch reconnectLatch = new CountDownLatch(1);

    private String lastUrl;



    public WebSocketConnector(){
    this.client = new OkHttpClient();
  }


    public void connect(String url) {
        this.lastUrl = url;
        connectInternal();
    }
    private synchronized void connectInternal(){
            if (webSocket != null) {
                try {
                    webSocket.close(1000, "reconnect");
                } catch (Exception ignored) {
                }
                webSocket = null;
            }
                Request request = new Request.Builder().url(lastUrl).build();
      this.webSocket = client.newWebSocket(request, new WebSocketListener() {

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
              autoReconnect();
              super.onFailure(webSocket, t, response);
          }

          @Override
          public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
              Message<?> msg = JsonUtils.fromJson(text, Message.class);
              switch (msg.getType()) {
                  case SYSTEM -> {
                        System.out.println("System message received.");
                  }
                  case MESSAGE -> {
                      System.out.println("Message from channel " + msg.getChannel());
                      System.out.println("Date: " + Instant.ofEpochMilli(msg.getTimestamp()));
                      System.out.println("Message: " + JsonUtils.toJson(msg.getData()));
                  }
              }

              super.onMessage(webSocket, text);
          }


          @Override
          public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
              super.onMessage(webSocket, bytes);
          }

          @Override
          public void onOpen(@NotNull WebSocket webSocket, Response response) {
              System.out.println("Connected, sending subscription...");
              reconnectAttemps.set(0);
              reconnectLatch.countDown();

//              SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
//                      .channel("kevin_updates")
//                      .id(1L)
//                      .tickInterval(10000)
//                      .build();
//              send(MessageType.SUBSCRIBE, subscriptionRequest);

//              String subscribeMsg = """
//                       {
//                                     "type": "subscribe",
//                                     "params": {
//                                       "id": 1,
//                                       "tick-interval": 10000,
//                                       "channel": "kevin_updates"
//                                     }
//                                   }
//                      """;
 //             webSocket.send(subscribeMsg);
 //             System.out.println("subscribed!");
          }

      });
    }

    public void autoReconnect(){
        int attempts = reconnectAttemps.incrementAndGet();
        if (attempts > MAX_RECONECT_ATTEMPTS) {
            System.out.println("Giving up on reconnecting");
            shutdown();
            System.exit(0);
            return;
        }
        if (attempts == 1){System.out.println("Connection failed: trying to reconnect...");}

        long delay = Math.min(MAX_DELAY_MS, RECONNECT_DELAY_MS * (1L << (attempts -1)));

        scheduler.schedule(()->{
            try {

                System.out.println("Reconnecting..." + " attempt " + attempts);
                connectInternal();
            }catch (Exception e){
                System.out.println("Reconnect failed: " + e);
                autoReconnect();
            }
        }, delay, TimeUnit.MILLISECONDS);
    }
    public void shutdown(){
        try {

            if (webSocket != null) {
                webSocket.close(1000, "shutdown");
            }
        }catch (Exception e) {
            System.out.println("Error during shutdown: " + e);

        }finally {
            scheduler.shutdownNow();

            client.dispatcher().executorService().shutdown();
        }
    }

    public void sendMessage(@NotNull Message message){
        webSocket.send(JsonUtils.toJson(message));
    }
    public <T> void send(MessageType messageType, T params){
        var message = Message.builder()
                .timestamp(System.currentTimeMillis())
                .type(messageType)
                .params(params)
                .build();
        sendMessage(message);
    }
    public void awaitReconnection() throws InterruptedException {
        reconnectLatch.await();
    }

}
