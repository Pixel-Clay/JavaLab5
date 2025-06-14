package clay.vehicle.dataStorage;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for retrieving file paths from environment variables. This class provides
 * functionality to convert environment variable values into valid file system paths.
 */
public class EnvPathRetriever {
  /**
   * Retrieves a file path from the specified environment variable. The environment variable should
   * contain a valid file system path.
   *
   * @param varName the name of the environment variable to retrieve
   * @return a Path object representing the file path from the environment variable
   * @throws NoEnvVarFoundException if the specified environment variable does not exist
   */
  public static Path getPath(String varName) throws NoEnvVarFoundException {
    String path = System.getenv(varName);
    if (path == null) {
      throw new NoEnvVarFoundException("Env var " + varName + " does not exist");
    }
    return Paths.get(path);
  }
}
