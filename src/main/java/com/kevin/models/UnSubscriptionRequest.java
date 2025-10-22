package com.kevin.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UnSubscriptionRequest {

    private Long id;

    @JsonProperty("tick-interval")
    private int tickInterval;

    private String channel;

}
