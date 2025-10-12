package com.kevin;

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

    OkHttpClient client = new OkHttpClient();

    connector.connect("ws://localhost:8081/ws/basic");



    client.dispatcher().executorService().shutdown();

  }
}

