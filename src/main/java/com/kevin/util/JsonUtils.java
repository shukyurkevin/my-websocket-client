package com.kevin.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kevin.expections.JsonParceException;

public class JsonUtils {
  public static final ObjectMapper MAPPER = new ObjectMapper();

  public static JsonNode readTree(String json){
    try {
      return MAPPER.readTree(json);
    } catch (JsonProcessingException e) {
      throw new JsonParceException("error while parsing JSON",e);
    }
  }

  public static <T> T fromJson(String json, Class<T> obj){

    try {
      return MAPPER.readValue(json, obj);
    } catch (JsonProcessingException e) {
      throw new JsonParceException("error while parsing JSON into object", e);
    }

  }
  public static String toJson(Object object){
      try {
          return MAPPER.writeValueAsString(object);
      } catch (JsonProcessingException e) {
          throw new JsonParceException("error while parsing object into JSON", e);
      }

  }

  public static <T> T fromJsonNode(JsonNode node, Class<T> clazz) throws Exception {
    JavaType type = MAPPER.getTypeFactory().constructType(clazz);
    return MAPPER.treeToValue(node, clazz);
  }

  
}
