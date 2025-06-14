package clay.vehicle.networking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.net.InetSocketAddress;

public class NetworkMessageSerializer {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static String serialize(NetworkMessage message) {
    try {
      ObjectNode node = objectMapper.createObjectNode();
      if (message.getType() != null) {
        node.put("type", message.getType().name());
      }
      if (message.getMessage() != null) {
        node.put("message", message.getMessage());
      }
      if (message.getCommand() != null) {
        node.put("command", message.getCommand());
      }
      if (message.getAddress() != null && message.getAddress() instanceof InetSocketAddress) {
        InetSocketAddress addr = (InetSocketAddress) message.getAddress();
        node.put("address", addr.getHostString() + ":" + addr.getPort());
      }
      if (message.getArgs() != null) {
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for (String arg : message.getArgs()) {
          arrayNode.add(arg);
        }
        node.set("args", arrayNode);
      }
      return objectMapper.writeValueAsString(node);
    } catch (Exception e) {
      throw new RuntimeException("Failed to serialize NetworkMessage", e);
    }
  }
}
