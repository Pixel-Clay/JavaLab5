package clay.vehicle.commands;

import clay.vehicle.Shell;
import clay.vehicle.networking.MessageType;
import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.NetworkMessageSerializer;
import jakarta.validation.ValidationException;

/**
 * Command implementation for inserting a new vehicle into the storage. This command prompts the
 * user for vehicle details and adds the new vehicle to the storage if all validations pass.
 */
public class Insert implements Executable {
  /** The storage instance where vehicles are stored */
  Shell shell;

  /**
   * Constructs a new Insert command with the specified storage.
   *
   * @param shell shell to ask user input
   */
  public Insert(Shell shell) {
    this.shell = shell;
  }

  /**
   * Executes the insert command. Prompts the user for vehicle details, validates them, and inserts
   * the new vehicle into the storage if validation passes.
   *
   * @param args command arguments (not used)
   * @return a message indicating the result of the operation
   */
  @Override
  public String execute(String[] args) {
    String[] v;
    try {
      v = MiscUtils.getVehicleSpecsFromInput(shell);
    } catch (ValidationException e) {
      return "! Format error: " + e.getMessage();
    }

    return NetworkMessageSerializer.serialize(
        NetworkMessage.newBuilder()
            .setType(MessageType.COMMAND)
            .setCommand("insert")
            .setArgs(v)
            .build());
  }
}
