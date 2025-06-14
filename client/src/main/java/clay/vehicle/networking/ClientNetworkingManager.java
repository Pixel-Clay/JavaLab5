package clay.vehicle.networking;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ClientNetworkingManager {
  byte[] send;
  byte[] receive;
  DatagramChannel dc;
  ByteBuffer snd, rec;
  SocketAddress addr;

  public ClientNetworkingManager(String hostname, int port)
      throws UnknownHostException, IOException {
    this.addr = new InetSocketAddress(Inet4Address.getByName(hostname), port);
    this.receive = new byte[65507]; // максимальный размер UDP пакета
    this.dc = DatagramChannel.open();
  }

  public void transmit(String message) throws IOException {
    send = message.getBytes();
    snd = ByteBuffer.wrap(send);
    dc.send(snd, addr);
    snd.clear();
  }

  public String receive() throws IOException {
    rec = ByteBuffer.wrap(receive);
    dc.receive(rec);
    rec.flip();
    int receivedLength = rec.limit();
    return new String(receive, 0, receivedLength);
  }
}
