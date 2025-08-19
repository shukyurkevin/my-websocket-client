package com.kevin.models;

import java.util.List;
import java.util.Map;

public class Subscri {
  public String type = "subscribe";
  public List<Map<String, Object>> params;

  public SubscribeRequest(){}
  public SubscribeRequest (List<Map<String, Object>> params) {
    this.params = params;
  }
}
