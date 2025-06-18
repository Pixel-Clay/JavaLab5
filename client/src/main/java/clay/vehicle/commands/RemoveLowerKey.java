package clay.vehicle.commands;

import clay.vehicle.networking.MessageType;
import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.NetworkMessageSerializer;

/**
 * Command implementation for removing vehicles with IDs less than a specified value. This command
 * removes all vehicles from the storage that have an ID less than the provided ID value.
 */
public class RemoveLowerKey implements Executable {

  /**
   * Executes the remove_lower_key command. Removes all vehicles with IDs less than the specified
   * ID.
   *
   * @param args command arguments, where args[0] is the ID threshold
   * @return a message indicating how many vehicles were removed
   */
  @Override
  public String execute(String[] args) {
    Integer id;
    try {
      id = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return "! Invalid id";
    } catch (ArrayIndexOutOfBoundsException e) {
      return "! Not enough arguments";
    }

    return NetworkMessageSerializer.serialize(
        NetworkMessage.newBuilder()
            .setType(MessageType.COMMAND)
            .setCommand("remove_lower_key")
            .setArgs(args)
            .build());
  }
}
