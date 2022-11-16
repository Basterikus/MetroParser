import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;


public class Main {

    public static String fileName = "data\\map.json";
    public static String metroURL = "https://skillbox-java.github.io/";

    public static void main(String[] args) throws IOException, ParseException {

        DataWriter dataWriter = new DataWriter();
        dataWriter.getDataIntoFile(fileName, metroURL);

        JSONParser parser = new JSONParser();
        JSONObject jsonData = (JSONObject) parser.parse(getJsonFile());
        JSONObject stationsObject = (JSONObject) jsonData.get("stations");

        stationsObject.keySet().stream().sorted(Comparator.comparingInt(s -> Integer.parseInt(((String)s)
                .replaceAll("[^\\d]", "")))).forEach(lineNumberObject ->
        {
            JSONArray stationsArray = (JSONArray) stationsObject.get(lineNumberObject);
            int stationsCount = stationsArray.size();
            System.out.println("Номер линиии " + lineNumberObject + " - колличество станций : " + stationsCount);
        });

    }
    public static String getJsonFile() {
        StringBuilder builder = new StringBuilder();
        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            lines.forEach(builder::append);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }
}
