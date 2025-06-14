package clay.vehicle.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Set;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerNetworkingManager {
  private final DatagramChannel channel;
  private final Selector selector;
  private final ByteBuffer receiveBuffer;
  private final ByteBuffer sendBuffer;
  private final InetSocketAddress socket;
  private boolean running;
  private static final Logger logger = LogManager.getLogger(ServerNetworkingManager.class);

  @Setter private ServerProcessingCallback readCallback;
  @Setter private ServerProcessingCallback writeCallback;

  public ServerNetworkingManager(int port) throws IOException {
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
    logger.info("Server running on port " + String.valueOf(getRunningPort()));
    while (running) {
      selector.select();
      Set<SelectionKey> selectedKeys = selector.selectedKeys();

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
    byte[] responseBytes = NetworkMessageSerializer.serialize(message).getBytes();
    sendBuffer.put(responseBytes);
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
      e.printStackTrace();
    }
  }

  public int getRunningPort() {
    return this.socket.getPort();
  }
}
