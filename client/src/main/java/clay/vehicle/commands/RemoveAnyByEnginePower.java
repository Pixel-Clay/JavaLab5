package clay.vehicle.commands;

import clay.vehicle.networking.MessageType;
import clay.vehicle.networking.NetworkMessage;
import clay.vehicle.networking.NetworkMessageSerializer;

public class RemoveAnyByEnginePower implements Executable {

  @Override
  public String execute(String[] args) {
    if (args.length == 0) return "! Not enough arguments";

    Float enginePower;
    try {
      enginePower = Float.parseFloat(args[0]);
    } catch (NumberFormatException e) {
      return "! Invalid argument";
    }

    return NetworkMessageSerializer.serialize(
        NetworkMessage.newBuilder()
            .setType(MessageType.COMMAND)
            .setCommand("remove_any_by_engine_power")
            .setArgs(args)
            .build());
  }
}
