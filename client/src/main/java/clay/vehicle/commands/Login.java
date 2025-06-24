package clay.vehicle.commands;

import clay.vehicle.ClientShell;

/**
 * Command implementation for removing vehicles that are less than a given example. This command
 * prompts for an example vehicle and removes all vehicles from the storage that are less than the
 * example according to their natural ordering.
 */
public class Login implements Executable {
  /** The storage instance where vehicles are stored */
  ClientShell shell;

  /** Constructs a new RemoveLower command with the specified storage. */
  public Login(ClientShell shell) {
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
    String login, password;
    login = MiscUtils.getaStringNotNull(shell, "login: ");
    password = MiscUtils.getaStringNotNull(shell, "password: ");

    if (login.contains(" ") || password.contains(" "))
      return "! login and password should not contain spaces";
    else {
      shell.setLogin(login, password);
      return "! Logged in as " + login;
    }
  }
}
