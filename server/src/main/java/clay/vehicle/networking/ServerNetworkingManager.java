package clay.vehicle.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerNetworkingManager {
  private DatagramChannel channel;
  private Selector selector;
  private ByteBuffer receiveBuffer;
  private ByteBuffer sendBuffer;
  private InetSocketAddress socket;
  private boolean running;
  private final int port;
  private static final Logger logger = LogManager.getLogger(ServerNetworkingManager.class);

  @Setter private ServerProcessingCallback readCallback;

  public ServerNetworkingManager(int port) {
    this.port = port;
  }

  public void init() throws IOException {
    this.channel = DatagramChannel.open();
    this.channel.configureBlocking(false);
    this.socket = new InetSocketAddress(port);
    this.channel.bind(this.socket);
    this.selector = Selector.open();
    this.channel.register(selector, SelectionKey.OP_READ);
    this.receiveBuffer = ByteBuffer.allocate(65507); // максимальный размер UDP пакета
    this.sendBuffer = ByteBuffer.allocate(65507);
    this.running = true;
  }

  public void run() throws IOException {
    logger.info("Server running on port " + getRunningPort());
    Set<SelectionKey> selectedKeys;
    while (running) {
      selector.select();

      try {
        selectedKeys = selector.selectedKeys();
      } catch (ClosedSelectorException e) {
        logger.warn("Socket is closed");
        continue;
      }

      for (var iter = selectedKeys.iterator(); iter.hasNext(); ) {
        SelectionKey key = iter.next();
        iter.remove();

        if (key.isValid()) {
          if (key.isReadable()) {
            handleRead(key);
          }
          if (key.isWritable()) {
            handleWrite(key);
          }
        }
      }
    }
  }

  private void handleRead(SelectionKey key) throws IOException {
    var sc = (DatagramChannel) key.channel();
    sc.register(key.selector(), SelectionKey.OP_WRITE);

    receiveBuffer.clear();
    SocketAddress clientAddress = sc.receive(receiveBuffer);

    if (clientAddress != null) {
      receiveBuffer.flip();
      int receivedLength = receiveBuffer.limit();
      String receivedMessage = new String(receiveBuffer.array(), 0, receivedLength);
      NetworkMessage message =
          NetworkMessageDeserializer.deserialize(receivedMessage, clientAddress);

      logger.info("Received from " + message.getAddress() + " " + receivedMessage);

      key.attach(this.readCallback.execute(message));
    }
  }

  private void handleWrite(SelectionKey key) throws IOException {
    var dc = (DatagramChannel) key.channel();
    NetworkMessage message = (NetworkMessage) key.attachment();
    sendBuffer.clear();
    sendBuffer.put(NetworkMessageSerializer.serialize(message).getBytes());
    sendBuffer.flip();

    if (message.hasAddress()) {
      logger.info("Sending reply to " + message.getAddress());
      dc.send(sendBuffer, message.getAddress());
    } else logger.warn("no address: " + NetworkMessageSerializer.serialize(message));
    dc.register(key.selector(), SelectionKey.OP_READ);
  }

  public void stop() {
    running = false;
    try {
      selector.close();
      channel.close();
    } catch (IOException e) {
      logger.error("IOException while stopping server: " + e.getMessage());
    }
  }

  public int getRunningPort() {
    return this.socket.getPort();
  }
}
