package clay.vehicle.dataStorage;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class CsvWriter {
    public static void writeVehicleStorageToCsv(VehicleStorage storage, File outputFile) throws IOException {
        CsvMapper csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule()); // Handles ZonedDateTime
        csvMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Define CSV schema based on VehicleStorageCsvRow
        CsvSchema schema = CsvSchema.builder()
                .addColumn("license_plate")
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
                .build().withHeader();

        // Write to CSV
        csvMapper.writer(schema).writeValue(outputFile, storage);
    }
}