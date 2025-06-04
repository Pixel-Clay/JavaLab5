package clay.vehicle.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Set;
import lombok.Setter;

public class ServerNetworkingManager {
  private final DatagramChannel channel;
  private final Selector selector;
  private final ByteBuffer receiveBuffer;
  private final ByteBuffer sendBuffer;
  private boolean running;

  @Setter private ServerProcessingCallback readCallback;
  @Setter private ServerProcessingCallback writeCallback;

  public ServerNetworkingManager(int port) throws IOException {
    this.channel = DatagramChannel.open();
    this.channel.configureBlocking(false);
    this.channel.bind(new InetSocketAddress(port));

    this.selector = Selector.open();
    this.channel.register(selector, SelectionKey.OP_READ);

    this.receiveBuffer = ByteBuffer.allocate(65507); // максимальный размер UDP пакета
    this.sendBuffer = ByteBuffer.allocate(65507);
    this.running = true;
  }

  public void start() throws IOException {
    while (running) {
      selector.select();
      Set<SelectionKey> selectedKeys = selector.selectedKeys();
      // Iterator<SelectionKey> iter = selectedKeys.iterator();

      for (var iter = selectedKeys.iterator(); iter.hasNext(); ) {
        SelectionKey key = iter.next();
        iter.remove();

        if (key.isValid()) {
          if (key.isAcceptable()) {
            handleAccept(key);
          }
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

  private void handleAccept(SelectionKey key) throws IOException {
    var sc = (DatagramChannel) key.channel();
    sc.configureBlocking(false);
    sc.register(key.selector(), SelectionKey.OP_READ);
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

      key.attach(this.readCallback.execute());
    }
  }

  private void handleWrite(SelectionKey key) throws IOException {
    var sc = (SocketChannel) key.channel();

    //        sc.write() //TODO: fix
  }

  public void transmit(NetworkMessage message) throws IOException {
    sendBuffer.clear();
    byte[] responseBytes = message.getMessage().getBytes();
    sendBuffer.put(responseBytes);
    sendBuffer.flip();
    channel.send(sendBuffer, message.getAddress());
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
}
