package Networking.Client;

import clay.vehicle.networking.networking.ClientNetworkingManager;
import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    ClientNetworkingManager mgr = new ClientNetworkingManager("localhost", 621);
    String message = "Hello world";
    mgr.transmit(message);
    System.out.println(mgr.receive());
  }
}
