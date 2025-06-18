package clay.vehicle.commands;

import clay.vehicle.Shell;

/**
 * Command implementation for exiting the application. This command terminates the program when
 * executed.
 */
public class Exit implements Executable {

  Shell shell;

  public Exit(Shell shell) {
    this.shell = shell;
  }

  /**
   * Executes the exit command. Terminates the program with exit code 0.
   *
   * @param args command arguments (not used)
   * @return an empty string (never reached as the program exits)
   */
  @Override
  public String execute(String[] args) {
    shell.setExitFlag();
    return "! Exiting...";
  }
}
