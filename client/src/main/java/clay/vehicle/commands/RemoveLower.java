package clay.vehicle.commands;

import clay.vehicle.Shell;
import clay.vehicle.networking.MessageType;
import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.NetworkMessageSerializer;
import jakarta.validation.ValidationException;

public class RemoveLower implements Executable {

  Shell shell;

  public RemoveLower(Shell shell) {
    this.shell = shell;
  }

  /**
   * Executes the remove_lower command. Prompts for an example vehicle and removes all vehicles that
   * are less than it.
   *
   * @param args command arguments (not used)
   * @return a message indicating how many vehicles were removed
   */
  @Override
  public String execute(String[] args) {
    String[] example;
    try {
      example = MiscUtils.getVehicleSpecsFromInput(shell);
    } catch (ValidationException e) {
      return "! Format error: " + e.getMessage();
    }

    return NetworkMessageSerializer.serialize(
        NetworkMessage.newBuilder()
            .setType(MessageType.COMMAND)
            .setCommand("remove_lower")
            .setArgs(example)
            .build());
  }
}
