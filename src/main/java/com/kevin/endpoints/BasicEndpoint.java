package com.kevin.endpoints;


import com.kevin.enums.MessageType;
import com.kevin.models.Message;
import com.kevin.models.SubscriptionRequest;
import com.kevin.util.JsonUtils;
import java.net.URI;
import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;


@ClientEndpoint
public class BasicEndpoint implements WebSocketEndpoint{

  private Session session;

  public BasicEndpoint(String uri) throws Exception {
    WebSocketContainer container = ContainerProvider.getWebSocketContainer();
    container.connectToServer(this, new URI(uri));
  }

  @Override
  @OnOpen
  public void onOpen(Session session, EndpointConfig config) {
    this.session = session;
    System.out.println("Connection opened with id " + session.getId());
  }

  @Override
  @OnMessage
  public void onMessage(String message, Session session) {

    System.out.println(message);
    Message<?> msg = JsonUtils.fromJson(message, Message.class);

    MessageType type = msg.getType();
    switch (type) {
      case ERROR -> System.out.println("Error: "+ msg.getParams());
      case MESSAGE -> {
        System.out.println("New message!");
        System.out.println("Channel: "+ msg.getChannel());
        System.out.println("Data: "+ msg.getData());
      }
      case SUBSCRIBE -> {
        System.out.println("Subscribed");
        System.out.println("Channel: "+ msg.getChannel());
        System.out.println("Data: "+ msg.getData());
      }
      case UNSUBSCRIBE -> {
        System.out.println("Unsubscribed");
        System.out.println("Channel: "+ msg.getChannel());
        System.out.println("Data: "+ msg.getData());
      }
    }



  }

  @Override
  @OnError
  public void onError(Session session, Throwable throwable) {
    System.err.println("Error " + throwable.getMessage());
  }


  public void sendMessage(String message) throws Exception {
    if (session != null && session.isOpen()){
      session.getBasicRemote().sendText(message);
    }
  }

  public void subscribe(Long id, int interval, String channel) throws Exception {
    SubscriptionRequest request = new SubscriptionRequest(id,interval, channel);
    Message<SubscriptionRequest> msg = Message.<SubscriptionRequest>builder()
        .type(MessageType.SUBSCRIBE)
        .timestamp(System.currentTimeMillis())
        .data(request)
        .build();

    String json = JsonUtils.toJson(msg);
    sendMessage(json);
    System.out.println("Sent subscribe request: "+ json);
  }

  public void unsubscribe(Long id, int interval, String channel) throws Exception{
    SubscriptionRequest request = new SubscriptionRequest(id, interval, channel);
    Message<SubscriptionRequest> msg = Message.<SubscriptionRequest>builder()
        .type(MessageType.UNSUBSCRIBE)
        .timestamp(System.currentTimeMillis())
        .data(request)
        .build();

    String json = JsonUtils.toJson(msg);
    sendMessage(json);
    System.out.println("Sent unsubscribe request: "+ json);
  }

  @Override
  @OnClose
  public void onClose(Session session, CloseReason reason) {
    System.out.println("Connection closed: " + reason);
  }

}
