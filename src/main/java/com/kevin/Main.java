package com.kevin;

import com.kevin.enums.MessageType;
import com.kevin.models.SubscriptionRequest;
import com.kevin.ws.WebSocketConnector;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws InterruptedException {


        WebSocketConnector connector = new WebSocketConnector();

        connector.connect("ws://localhost:8081/ws/basic");

        connector.awaitReconnection();

        Scanner scanner = new Scanner(System.in);
        SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder()
                .channel("kevin_updates")
                .id(1L)
                .tickInterval(5000)
                .build();


        while (true) {
            System.out.println("Enter 1 to subscribe, 2 to unsubscribe, or 0 to exit:");
            int choice = scanner.nextInt();
            if (choice == 0) {
                System.out.println("Exiting...");
                System.exit(0);
            }
            switch (choice) {
                case 1:
                    connector.send(MessageType.SUBSCRIBE, subscriptionRequest);
                    break;
                case 2:
                    connector.send(MessageType.UNSUBSCRIBE, subscriptionRequest);
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }
}

      //                       {
//                                     "type": "subscribe",
//                                     "params": {
//                                       "id": 1,
//                                       "tick-interval": 10000,
//                                       "channel": "kevin_updates"
//                                     }



