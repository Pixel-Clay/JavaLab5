package clay.vehicle.commands;

import clay.vehicle.Shell;

/**
 * Interface for executable commands in the Vehicle Management System. All command implementations
 * must implement this interface to be recognized and executed by the command processor.
 */
public interface Executable {
  /**
   * Executes the command with the given arguments.
   *
   * @param args Array of command arguments
   * @return A string message with command output
   */
  String execute(String[] args);

  /**
   * Attaches a shell instance to the command for input/output operations.
   *
   * @param newShell The shell instance to attach
   */
  void attachShell(Shell newShell);
}
