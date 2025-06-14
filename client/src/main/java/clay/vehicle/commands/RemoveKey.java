package clay.vehicle.commands;

import clay.vehicle.networking.MessageType;
import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.NetworkMessageSerializer;

/**
 * Command implementation for removing a vehicle by its key. This command removes a vehicle from the
 * storage using its key (ID).
 */
public class RemoveKey implements Executable {

  /**
   * Executes the remove_key command. Removes a vehicle from the storage using the provided key.
   *
   * @param args command arguments, where args[0] is the key of the vehicle to remove
   * @return a message indicating the result of the removal operation
   */
  @Override
  public String execute(String[] args) {
    Integer key;
    if (args.length == 0) return "! Not enough arguments";
    try {
      key = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return "! Not a number";
    }

    return NetworkMessageSerializer.serialize(
        NetworkMessage.newBuilder()
            .setType(MessageType.COMMAND)
            .setCommand("remove_key")
            .setArgs(args)
            .build());
  }
}
