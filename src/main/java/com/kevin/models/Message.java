package com.kevin.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kevin.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message<T> {
  private MessageType type;
  private String channel;
  private Long timestamp;
  private Long sequenceId;
  private Object result;
  private T data;
  private Object params;



}
