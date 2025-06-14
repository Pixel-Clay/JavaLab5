package clay.vehicle.commands;

import clay.vehicle.networking.MessageType;
import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.NetworkMessageSerializer;

public class GroupCountungByCoordinates implements Executable {

  @Override
  public String execute(String[] args) {
    return NetworkMessageSerializer.serialize(
        NetworkMessage.newBuilder()
            .setType(MessageType.COMMAND)
            .setCommand("group_counting_by_coordinates")
            .build());
  }
}
