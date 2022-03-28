package com.whatsapp.api_gateway.controller;

import java.util.function.Function;

import com.whatsapp.api_gateway.models.Message;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class SocketController {
      private final SimpMessagingTemplate template;
      private final WebClient webClient;
      /**
       * @param message message sent from client
       * @description this method is called when a message is sent from client
       */
      @MessageMapping("/chat")
      public void getMessageFromClient(Message message) {
            //save to db
            webClient
            .post()
            .uri("/messages")
            .body(Mono.just(message), Message.class)
            .exchangeToMono(res-> res.bodyToMono(Message.class))
            //send to friend message and send client single tick message 
            .map(savedMessage -> {
                  //friend
                  template.convertAndSendToUser(message.getSentTo(), "/topic/chat", message);
                  //single tick
                  template.convertAndSendToUser(message.getSentBy(), "/topic/chat", message);
                  return savedMessage;
            });
      }
      /**
       * @param message message sent from friend
       * @description this method is called when a message is confirmed by friend
       */
      @MessageMapping("/chat")
      public void confirmFriendReceivedMessage(Message message) {
            webClient
            .put()
            .uri("/messages/{id}", message.getId())
            .body(Mono.empty(),Void.class)
            .retrieve()
            .toBodilessEntity()
            .map((Function<ResponseEntity<Void>, Void>) res -> {
                  //message to client double click
                  template.convertAndSendToUser(message.getSentTo(), "/topic/chat", message);
                  return null;
            });
      }
}
