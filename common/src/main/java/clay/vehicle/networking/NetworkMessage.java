package clay.vehicle.networking;

import java.net.SocketAddress;
import lombok.Getter;

@Getter
public class NetworkMessage {
  private MessageType type;
  private String message;
  private SocketAddress address;
  private String[] args;

  private NetworkMessage() {}

  public static Builder newBuilder() {
    return new NetworkMessage().new Builder();
  }

  public class Builder {
    private Builder() {}

    public Builder setType(MessageType type) {
      NetworkMessage.this.type = type;
      return this;
    }

    public Builder setMessage(String message) {
      NetworkMessage.this.message = message;
      return this;
    }

    public Builder setAdress(SocketAddress address) {
      NetworkMessage.this.address = address;
      return this;
    }

    public Builder setArgs(String[] args) {
      NetworkMessage.this.args = args;
      return this;
    }

    public NetworkMessage build() {
      return NetworkMessage.this;
    }
  }
}
