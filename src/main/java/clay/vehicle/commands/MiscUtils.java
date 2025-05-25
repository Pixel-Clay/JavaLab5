package clay.vehicle.commands;

import clay.vehicle.Shell;
import clay.vehicle.dataStorage.VehicleStorage;
import clay.vehicle.vehicles.Coordinates;
import clay.vehicle.vehicles.FuelType;
import clay.vehicle.vehicles.Vehicle;
import clay.vehicle.vehicles.VehicleType;
import jakarta.validation.*;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Utility class providing helper methods for vehicle-related operations. This class contains
 * methods for parsing input, converting strings to enums, and creating vehicles from user input.
 */
public class MiscUtils {

  /**
   * Parses an array of key-value pairs into a map. Each string in the array should be in the format
   * "key=value".
   *
   * @param s array of strings in key=value format
   * @return a map containing the parsed key-value pairs
   */
  public static Map<String, String> parseKeyValue(String[] s) {
    String[] pair;
    Map<String, String> keyValueMap = new HashMap<>();

    for (String argument : s) {
      pair = argument.split("=");
      keyValueMap.put(pair[0], pair[1]);
    }

    return keyValueMap;
  }

  /**
   * Converts a string to a FuelType enum value.
   *
   * @param s the string to convert
   * @return the corresponding FuelType, or null if the string doesn't match any enum value
   */
  public static FuelType stringToFuelType(String s) {
    switch (s) {
      case "alcohol":
        return FuelType.ALCOHOL;

      case "manpower":
        return FuelType.MANPOWER;

      case "nuclear":
        return FuelType.NUCLEAR;
    }
    return null;
  }

  /**
   * Converts a string to a VehicleType enum value.
   *
   * @param s the string to convert
   * @return the corresponding VehicleType, or null if the string doesn't match any enum value
   */
  public static VehicleType stringToVehicleType(String s) {
    switch (s) {
      case "car":
        return VehicleType.CAR;
      case "plane":
        return VehicleType.PLANE;
      case "helicopter":
        return VehicleType.HELICOPTER;
      case "hoverboard":
        return VehicleType.HOVERBOARD;
      case "spaceship":
        return VehicleType.SPACESHIP;
    }
    return null;
  }

  /**
   * Prompts the user for a non-null Double value. Keeps prompting until a valid number is entered.
   *
   * @param shell the shell instance for input/output
   * @param invitation the prompt message to display
   * @return the entered Double value
   */
  public static Double getaDoubleNotNull(Shell shell, String invitation) {
    Double x;
    while (true) {
      String inp = shell.getInput("Insert " + invitation);
      try {
        x = Double.valueOf(inp);
        break;
      } catch (NumberFormatException e) {
        System.out.println("! Not a number");
      } catch (NullPointerException e) {
        System.out.println("! " + invitation + " can't be null");
      }
    }
    return x;
  }

  /**
   * Prompts the user for a non-null Float value. Keeps prompting until a valid number is entered.
   *
   * @param shell the shell instance for input/output
   * @param invitation the prompt message to display
   * @return the entered Float value
   */
  public static Float getaFloatNotNull(Shell shell, String invitation) {
    Float x;
    while (true) {
      String inp = shell.getInput("Insert " + invitation);
      try {
        x = Float.valueOf(inp);
        break;
      } catch (NumberFormatException e) {
        System.out.println("! Not a number");
      } catch (NullPointerException e) {
        System.out.println("! " + invitation + " can't be null");
      }
    }
    return x;
  }

  /**
   * Prompts the user for a non-null VehicleType value. Keeps prompting until a valid vehicle type
   * is entered.
   *
   * @param shell the shell instance for input/output
   * @param invitation the prompt message to display
   * @return the entered VehicleType value
   */
  public static VehicleType getaVehicleType(Shell shell, String invitation) {
    VehicleType x;
    while (true) {
      String inp = shell.getInput("Insert " + invitation);
      if (inp == null) return null;
      else {
        x = stringToVehicleType(inp);
        if (x == null) System.out.println("! " + invitation + "input not in enum");
        else break;
      }
    }
    return x;
  }

  /**
   * Prompts the user for a non-null FuelType value. Keeps prompting until a valid fuel type is
   * entered.
   *
   * @param shell the shell instance for input/output
   * @param invitation the prompt message to display
   * @return the entered FuelType value
   */
  public static FuelType getaFuelTypeNotNull(Shell shell, String invitation) {
    FuelType x;
    while (true) {
      String inp = shell.getInput("Insert " + invitation);
      if (inp == null) System.out.println("! " + invitation + "can't be null");
      else {
        x = stringToFuelType(inp);
        if (x == null) System.out.println("! " + invitation + "input not in enum");
        else break;
      }
    }
    return x;
  }

  /**
   * Prompts the user for a non-null String value. Keeps prompting until a non-null string is
   * entered.
   *
   * @param shell the shell instance for input/output
   * @param invitation the prompt message to display
   * @return the entered String value
   */
  public static String getaStringNotNull(Shell shell, String invitation) {
    String inp;
    while (true) {
      inp = shell.getInput("Insert " + invitation);
      if (inp == null) System.out.println("! " + invitation + "can't be null");
      else break;
    }
    return inp;
  }

  /**
   * Creates a new Vehicle from user input. Prompts the user for all required vehicle attributes and
   * validates them.
   *
   * @param shell the shell instance for input/output
   * @param storage the storage instance to get the next available ID
   * @return a new Vehicle with the entered attributes
   * @throws ValidationException if the entered attributes don't satisfy validation constraints
   */
  public static Vehicle getaVehicleFromInput(Shell shell, VehicleStorage storage)
      throws ValidationException {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    String name = getaStringNotNull(shell, "name: ");
    Double x = getaDoubleNotNull(shell, "x coordinate: ");
    Double y = getaDoubleNotNull(shell, "y coordinate: ");
    Float enginePower = getaFloatNotNull(shell, "engine power: ");
    Float distanceTravelled = getaFloatNotNull(shell, "distance travelled: ");
    VehicleType vehicleType =
        getaVehicleType(shell, "vehicle type (car, plane, helicopter, hoverboard, spaceship): ");
    FuelType fuelType = getaFuelTypeNotNull(shell, "fuel type (alcohol, manpower, nuclear): ");

    Vehicle v =
        new Vehicle(
            storage.getNextId(),
            name,
            new Coordinates(x, y),
            ZonedDateTime.now(),
            enginePower,
            distanceTravelled,
            vehicleType,
            fuelType);

    Set<ConstraintViolation<Vehicle>> violations = validator.validate(v);
    if (!violations.isEmpty()) throw new ValidationException(violations.toString());
    else return v;
  }
}
