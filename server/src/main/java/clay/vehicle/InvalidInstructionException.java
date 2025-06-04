package clay.vehicle;

/**
 * Exception thrown when an invalid or unknown command instruction is encountered during command
 * processing.
 */
public class InvalidInstructionException extends Exception {
  /**
   * Constructs a new InvalidInstructionException with the specified detail message.
   *
   * @param s the detail message describing the invalid instruction
   */
  public InvalidInstructionException(String s) {
    super(s);
  }
}
