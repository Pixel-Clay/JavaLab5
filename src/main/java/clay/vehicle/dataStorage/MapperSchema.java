package clay.vehicle.dataStorage;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;

/**
 * Defines the schema for CSV mapping of vehicle data. This record contains the structure of the CSV
 * file used for storing vehicle information, including all required fields and their order.
 */
public record MapperSchema() {
  /**
   * The CSV schema definition for vehicle data. Defines the columns and their order in the CSV
   * file: - id: Unique identifier for the vehicle - name: Name of the vehicle - x, y: Coordinates
   * of the vehicle - creationDate: Date when the vehicle was created - enginePower: Power of the
   * vehicle's engine - distanceTravelled: Total distance travelled by the vehicle - type: Type of
   * the vehicle - fuelType: Type of fuel used by the vehicle - init_date: Initialization date of
   * the vehicle record
   */
  public static final CsvSchema schema =
      CsvSchema.builder()
          .addColumn("id")
          .addColumn("name")
          .addColumn("x")
          .addColumn("y")
          .addColumn("creationDate")
          .addColumn("enginePower")
          .addColumn("distanceTravelled")
          .addColumn("type")
          .addColumn("fuelType")
          .addColumn("init_date")
          .build()
          .withHeader();
}
