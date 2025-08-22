package com.kevin.endpoints;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

public interface WebSocketEndpoint {

  void onOpen(Session session, EndpointConfig config);

  void onMessage(String message, Session session);

  void onError(Session session, Throwable throwable);

  void onClose(Session session, CloseReason reason);
}
