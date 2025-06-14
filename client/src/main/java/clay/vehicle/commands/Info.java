package clay.vehicle.commands;

import clay.vehicle.networking.MessageType;
import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.NetworkMessageSerializer;

/**
 * Command implementation for displaying information about the vehicle storage. This command shows
 * the initialization date, type, and number of vehicles in the storage.
 */
public class Info implements Executable {

  @Override
  public String execute(String[] args) {
    return NetworkMessageSerializer.serialize(
        NetworkMessage.newBuilder().setType(MessageType.COMMAND).setCommand("info").build());
  }
}
