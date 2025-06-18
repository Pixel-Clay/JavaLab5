package clay.vehicle.networking;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientNetworkingManager {
  private byte[] send;
  private byte[] receive;
  private DatagramChannel dc;
  private ByteBuffer snd;
  private SocketAddress addr;
  private String hostname;
  private int port;

  public ClientNetworkingManager(String hostname, int port) {
    this.receive = new byte[65507]; // максимальный размер UDP пакета
    this.hostname = hostname;
    this.port = port;
  }

  public void init() throws UnknownHostException, IOException {
    this.addr = new InetSocketAddress(Inet4Address.getByName(hostname), port);
    this.dc = DatagramChannel.open();
    this.dc.configureBlocking(true); // Ensure blocking mode
  }

  public void setTimeout(int timeMs) throws SocketException, IOException {
    dc.socket().setSoTimeout(timeMs);
    dc.configureBlocking(true);
  }

  public void transmit(String message) throws IOException {
    send = message.getBytes();
    snd = ByteBuffer.wrap(send);
    dc.send(snd, addr);
    snd.clear();
  }

  public String receive() throws SocketTimeoutException, IOException {
    DatagramPacket dp = new DatagramPacket(receive, receive.length);
    dc.socket().receive(dp);

    return new String(dp.getData(), 0, dp.getLength());
  }
}
