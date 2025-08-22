package com.kevin.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageType {
  @JsonProperty("message") MESSAGE,
  @JsonProperty("system") SYSTEM,
  @JsonProperty("error") ERROR,
  @JsonProperty("subscribe") SUBSCRIBE,
  @JsonProperty("unsubscribe") UNSUBSCRIBE

}
