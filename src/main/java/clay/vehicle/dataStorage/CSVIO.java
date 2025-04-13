package clay.vehicle.dataStorage;

import java.io.*;
import java.util.HashMap;

public class CSVIO {

    public static HashMap<Integer, String[]> readCSV(String csvFile) throws IOException {
        HashMap<Integer, String[]> records = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(csvFile));
        String row;
        while ((row = reader.readLine()) != null) {
            String[] values = row.split(",");
            if (values.length > 0) {
                int id = Integer.parseInt(values[0].trim());
                String[] data = new String[values.length - 1];
                System.arraycopy(values, 1, data, 0, data.length);
                records.put(id, data);
            }
        }

        reader.close();

        return records;
    }

    public static void writeCSV(String csvfile, HashMap<Integer, String[]> records) throws IOException {
        FileWriter writer = new FileWriter(csvfile);
        for (int key : records.keySet()) {
            String row = key + String.join(",", records.get(key)) + "\n";
            writer.write(row); // Write the content to the file
        }

        writer.close();
    }
}