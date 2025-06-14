package clay.vehicle.commands;

import clay.vehicle.Shell;
import clay.vehicle.networking.MessageType;
import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.NetworkMessageSerializer;
import jakarta.validation.ValidationException;

/**
 * Command implementation for updating an existing vehicle. This command updates a vehicle with the
 * specified ID by replacing it with a new vehicle that has the same ID but potentially different
 * attributes.
 */
public class Update implements Executable {
  Shell shell;

  public Update(Shell shell) {
    this.shell = shell;
  }

  @Override
  public String execute(String[] args) {
    Integer id;
    try {
      id = Integer.parseInt(args[0]);
    } catch (NumberFormatException e) {
      return "! id is not valid";
    } catch (ArrayIndexOutOfBoundsException e) {
      return "! Not enough arguments";
    }

    String[] update;
    try {
      update = MiscUtils.getVehicleSpecsFromInput(shell);
    } catch (ValidationException e) {
      return "! Format error: " + e.getMessage();
    }

    return NetworkMessageSerializer.serialize(
        NetworkMessage.newBuilder()
            .setType(MessageType.COMMAND)
            .setCommand("update")
            .setArgs(MiscUtils.concatenateArrays(args, update))
            .build());
  }
}
