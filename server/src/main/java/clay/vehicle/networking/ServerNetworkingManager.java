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
import java.util.concurrent.ForkJoinPool;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerNetworkingManager {
  private DatagramChannel channel;
  private Selector selector;
  private ByteBuffer receiveBuffer;
  private InetSocketAddress socket;
  private boolean running;
  private final int port;
  private static final Logger logger = LogManager.getLogger(ServerNetworkingManager.class);
  private final ForkJoinPool readPool = ForkJoinPool.commonPool();
  private final ForkJoinPool writePool = ForkJoinPool.commonPool();

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
  }

  public void run() throws IOException {
    this.running = true;
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
    receiveBuffer.clear();
    DatagramChannel sc = (DatagramChannel) key.channel();
    SocketAddress clientAddress = sc.receive(receiveBuffer);

    if (clientAddress != null) {
      receiveBuffer.flip();
      int receivedLength = receiveBuffer.limit();
      byte[] rawBytes = new byte[receivedLength];
      receiveBuffer.get(rawBytes);

      logger.info("Received " + receivedLength + " bytes from " + clientAddress);

      // Offload deserialization and processing to ForkJoinPool
      readPool.execute(
          () -> {
            try {
              String receivedMessage = new String(rawBytes);
              NetworkMessage message =
                  NetworkMessageDeserializer.deserialize(receivedMessage, clientAddress);

              logger.info("Parsed request from " + clientAddress + ": " + receivedMessage);

              Object response = this.readCallback.execute(message);

              synchronized (key) {
                key.attach(response);
                key.interestOps(SelectionKey.OP_WRITE);
              }
              selector.wakeup();
            } catch (Exception e) {
              logger.error("Exception in message processing: " + e.getMessage(), e);
            }
          });
    }
  }

  private void handleWrite(SelectionKey key) {
    SocketAddress address;
    NetworkMessage message = (NetworkMessage) key.attachment();

    DatagramChannel dc = (DatagramChannel) key.channel();
    address = message.hasAddress() ? message.getAddress() : null;

    // Remove OP_WRITE immediately to prevent duplicate tasks
    synchronized (key) {
      key.interestOps(SelectionKey.OP_READ);
    }

    // Offload serialization and sending to ForkJoinPool
    writePool.execute(
        () -> {
          try {
            ByteBuffer localSendBuffer = ByteBuffer.allocate(65507);
            byte[] data = NetworkMessageSerializer.serialize(message).getBytes();
            localSendBuffer.put(data);
            localSendBuffer.flip();

            if (address != null) {
              logger.info("Sending reply to " + address);
              dc.send(localSendBuffer, address);
            } else {
              logger.warn(
                  "Reply with no address: "
                      + NetworkMessageSerializer.serialize(message)
                      + ", skipping transmission");
            }
            selector.wakeup();
          } catch (IOException e) {
            logger.error("IOException while sending response: " + e.getMessage(), e);
          }
        });
  }

  public void stop() {
    running = false;
    try {
      readPool.shutdown();
      writePool.shutdown();
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
