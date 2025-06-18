package clay.vehicle.commands;

import clay.vehicle.Shell;
import clay.vehicle.networking.MessageType;
import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.NetworkMessageSerializer;
import jakarta.validation.ValidationException;

public class ReplaceIfHigher implements Executable {

  Shell shell;

  public ReplaceIfHigher(Shell shell) {
    this.shell = shell;
  }

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
    String[] replace;
    try {
      replace = MiscUtils.getVehicleSpecsFromInput(shell);
    } catch (ValidationException e) {
      return "! Format error: " + e.getMessage();
    }
    return NetworkMessageSerializer.serialize(
        NetworkMessage.newBuilder()
            .setType(MessageType.COMMAND)
            .setMessage("replace_if_greater")
            .setArgs(MiscUtils.concatenateArrays(args, replace))
            .build());
  }
}
