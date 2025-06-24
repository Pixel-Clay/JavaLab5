package clay.vehicle.networking;

import java.net.SocketAddress;
import lombok.Getter;

@SuppressWarnings("unused")
@Getter
public class NetworkMessage {
  private SocketAddress address;
  private MessageType type;
  private String command;
  private String[] args;
  private String message;
  private String login;
  private String password;

  public boolean hasMessage() {
    return message != null;
  }

  public boolean hasAddress() {
    return address != null;
  }

  public boolean hasArgs() {
    return args != null;
  }

  public boolean hasCommand() {
    return command != null;
  }

  public boolean hasLogin() {
    return login != null;
  }

  public boolean hasPassword() {
    return password != null;
  }

  public void setAuth(String login, String password) {
    this.login = login;
    this.password = password;
  }

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

    public Builder setCommand(String command) {
      NetworkMessage.this.command = command;
      return this;
    }

    public Builder setLogin(String login) {
      NetworkMessage.this.login = login;
      return this;
    }

    public Builder setPassword(String password) {
      NetworkMessage.this.password = password;
      return this;
    }

    public NetworkMessage build() {
      return NetworkMessage.this;
    }
  }
}
