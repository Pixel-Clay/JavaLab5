package clay.vehicle.networking;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class NetworkMessageDeserializer {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static NetworkMessage deserialize(String json) {
    try {
      JsonNode node = objectMapper.readTree(json);
      NetworkMessage.Builder builder = NetworkMessage.newBuilder();

      if (node.hasNonNull("type")) {
        builder.setType(MessageType.valueOf(node.get("type").asText()));
      }
      if (node.hasNonNull("message")) {
        builder.setMessage(node.get("message").asText());
      }
      if (node.hasNonNull("command")) {
        builder.setCommand(node.get("command").asText());
      }
      if (node.hasNonNull("address")) {
        String addrStr = node.get("address").asText();
        String[] parts = addrStr.split(":");
        if (parts.length == 2) {
          String host = parts[0];
          int port = Integer.parseInt(parts[1]);
          SocketAddress address = new InetSocketAddress(host, port);
          builder.setAdress(address);
        }
      }
      if (node.hasNonNull("args") && node.get("args").isArray()) {
        JsonNode argsNode = node.get("args");
        String[] args = new String[argsNode.size()];
        for (int i = 0; i < argsNode.size(); i++) {
          args[i] = argsNode.get(i).asText();
        }
        builder.setArgs(args);
      } else {
        builder.setArgs(null);
      }
      if (node.hasNonNull("login")) {
        builder.setLogin(node.get("login").asText());
      }
      if (node.hasNonNull("password")) {
        builder.setPassword(node.get("password").asText());
      }
      return builder.build();
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize NetworkMessage", e);
    }
  }

  public static NetworkMessage deserialize(String json, SocketAddress address) {
    try {
      JsonNode node = objectMapper.readTree(json);
      NetworkMessage.Builder builder = NetworkMessage.newBuilder();

      if (node.hasNonNull("type")) {
        builder.setType(MessageType.valueOf(node.get("type").asText()));
      }
      if (node.hasNonNull("message")) {
        builder.setMessage(node.get("message").asText());
      }
      if (node.hasNonNull("command")) {
        builder.setCommand(node.get("command").asText());
      }
      if (address != null) {
        builder.setAdress(address);
      }
      if (node.hasNonNull("args") && node.get("args").isArray()) {
        JsonNode argsNode = node.get("args");
        String[] args = new String[argsNode.size()];
        for (int i = 0; i < argsNode.size(); i++) {
          args[i] = argsNode.get(i).asText();
        }
        builder.setArgs(args);
      } else builder.setArgs(null);
      if (node.hasNonNull("login")) {
        builder.setLogin(node.get("login").asText());
      }
      if (node.hasNonNull("password")) {
        builder.setPassword(node.get("password").asText());
      }
      return builder.build();
    } catch (Exception e) {
      throw new RuntimeException("Failed to deserialize NetworkMessage", e);
    }
  }
}
