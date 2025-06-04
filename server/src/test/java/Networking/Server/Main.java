package Networking.Server;

import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.ServerNetworkingManager;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    ServerNetworkingManager mgr = new ServerNetworkingManager(621);
    NetworkMessage in = mgr.receive();
    String response = in.getMessage() + " : Welcome " + in.getMessage();
    mgr.transmit(
        NetworkMessage.newBuilder().setAdress(in.getAddress()).setMessage(response).build());
  }
}
