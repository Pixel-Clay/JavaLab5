package clay.vehicle.dataStorage;

import clay.vehicle.vehicles.Vehicle;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.File;

import java.util.HashMap;

public class Serializer {
    public static void write(HashMap<Integer, Vehicle> storage, String path) throws Exception {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(VehicleStorage.class).withHeader();

        mapper.writer(schema).writeValue(new File(path), storage);
    }
}
