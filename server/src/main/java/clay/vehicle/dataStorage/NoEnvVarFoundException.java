package clay.vehicle.dataStorage;

/**
 * Exception thrown when a required environment variable cannot be found. This exception is
 * typically thrown when the system cannot locate environment variables needed for data storage
 * operations.
 */
public class NoEnvVarFoundException extends RuntimeException {
  /**
   * Constructs a new NoEnvVarFoundException with the specified detail message.
   *
   * @param message the detail message describing which environment variable was not found
   */
  public NoEnvVarFoundException(String message) {
    super(message);
  }
}
