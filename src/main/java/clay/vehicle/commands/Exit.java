package clay.vehicle.commands;

import clay.vehicle.Shell;

/**
 * Command implementation for exiting the application. This command terminates the program when
 * executed.
 */
public class Exit implements Executable {

  /**
   * Executes the exit command. Terminates the program with exit code 0.
   *
   * @param args command arguments (not used)
   * @return an empty string (never reached as the program exits)
   */
  @Override
  public String execute(String[] args) {
    System.exit(0);
    return "";
  }

  /**
   * Attaches a shell instance to this command. This command doesn't require shell access, so this
   * method is empty.
   *
   * @param newShell the shell instance to attach (not used)
   */
  @Override
  public void attachShell(Shell newShell) {}
}
