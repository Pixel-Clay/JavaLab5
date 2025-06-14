package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.Coordinates;
import clay.vehicle.vehicles.Vehicle;
import clay.vehicle.vehicles.VehicleType;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.cfg.*;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * Utility class for reading vehicle data from CSV files. This class provides functionality to parse
 * CSV files containing vehicle information and convert them into VehicleStorage objects. It handles
 * validation of the data and ensures data consistency.
 */
public class CsvReader {
  /**
   * Reads vehicle data from a CSV file and creates a VehicleStorage object. The method performs the
   * following steps: 1. Reads the CSV file using the predefined schema 2. Validates the consistency
   * of initialization dates 3. Validates each vehicle's data 4. Ensures unique vehicle IDs 5.
   * Creates and populates a VehicleStorage object
   *
   * @param inputFile the path to the CSV file to read
   * @return a VehicleStorage object containing the parsed vehicle data
   * @throws IOException if there are issues reading the file or if vehicle IDs are not unique
   * @throws ValidationException if the CSV data has inconsistent initialization dates
   * @throws RuntimeException if there are issues setting the initialization date
   */
  public static VehicleStorage readVehicleStorageFromCsv(Path inputFile) throws IOException {
    CsvMapper csvMapper = new CsvMapper();
    csvMapper.registerModule(new JavaTimeModule()); // Handles ZonedDateTime
    csvMapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
    csvMapper
        .coercionConfigFor(VehicleType.class)
        .setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);

    // Define CSV schema based on the header
    CsvSchema schema = MapperSchema.schema;

    File csvFile = inputFile.toFile();

    MappingIterator<VehicleStorageCsvRow> rows =
        csvMapper.readerFor(VehicleStorageCsvRow.class).with(schema).readValues(csvFile);

    List<VehicleStorageCsvRow> rowList = rows.readAll();
    if (rowList.isEmpty()) {
      return new VehicleStorage(); // Return empty storage
    }

    // Check if all rows have the same init_date
    ZonedDateTime initDate = rowList.get(0).getInitDate();
    if (!rowList.stream().allMatch(row -> row.getInitDate().equals(initDate))) {
      throw new ValidationException("CSV has inconsistent init_date values");
    }

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();

    // Rebuild VehicleStorage
    VehicleStorage storage = new VehicleStorage();
    HashMap<Integer, Vehicle> storageMap = new HashMap<>();
    List<Integer> ids = new ArrayList<>();

    for (VehicleStorageCsvRow row : rowList) {
      Vehicle vehicle = getVehicle(row);
      Set<ConstraintViolation<Vehicle>> violations = validator.validate(vehicle);
      if (!violations.isEmpty()) continue;

      ids.add(vehicle.getId());

      storageMap.put(vehicle.getId(), vehicle);
    }

    Set<Object> uniqueIds = new HashSet<>(ids);

    if (ids.size() != uniqueIds.size()) {
      throw new IOException("IDs are not unique");
    }

    // Use reflection to set final fields (initDate)
    try {
      Field initDateField = VehicleStorage.class.getDeclaredField("initDate");
      initDateField.setAccessible(true);
      initDateField.set(storage, initDate);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException("Failed to set initDate", e);
    }

    // Set storage
    storage.setStorage(storageMap);
    storage.setInitDate(initDate);

    return storage;
  }

  /**
   * Creates a Vehicle object from a CSV row. This method reconstructs a Vehicle object with all its
   * properties from the data in a VehicleStorageCsvRow.
   *
   * @param row the CSV row containing vehicle data
   * @return a new Vehicle object with the data from the row
   */
  private static Vehicle getVehicle(VehicleStorageCsvRow row) {
    Coordinates coordinates = new Coordinates(row.getCoordX(), row.getCoordY());

    // Rebuild Vehicle
    Vehicle vehicle =
        new Vehicle(
            row.getVehicleId(),
            row.getVehicleName(),
            coordinates,
            row.getCreationDate(),
            row.getEnginePower(),
            row.getDistanceTravelled(),
            row.getVehicleType(),
            row.getFuelType());
    vehicle.setCreationDate(row.getCreationDate());
    return vehicle;
  }
}
