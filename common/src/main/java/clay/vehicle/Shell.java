package clay.vehicle;

import clay.vehicle.commands.Executable;

public interface Shell {
  void setExitFlag();

  void attachCommand(Executable command, String name);

  String getInput(String invitation);

  void run();

  CommandProcessor getProcessor();
}
