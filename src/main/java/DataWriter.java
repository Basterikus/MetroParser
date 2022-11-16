import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;

public class DataWriter {
    private final JSONObject metro = new JSONObject();

    public void getDataIntoFile(String fileName, String metroURL) throws IOException {
        getLinesFromURL(metroURL);
        getStationsFromURL(metroURL);
        jsonWriter(fileName);
        System.out.println("Данные успешно записаны в файл - " + fileName);
    }

    private void jsonWriter(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(metro.toJSONString());
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getLinesFromURL(String metroURL) throws IOException {
        Document doc = Jsoup.connect(metroURL).maxBodySize(0).get();
        JSONArray linesArray = new JSONArray();
        Elements lineElements = doc.select(".js-metro-line");
        lineElements.forEach(element -> {
            String lineNumber = element.attr("data-line");
            String lineName = element.text();
            JSONObject lines = new JSONObject();
            lines.put("number", lineNumber);
            lines.put("name", lineName);
            linesArray.add(lines);
        });
        metro.put("lines", linesArray);
    }

    private void getStationsFromURL(String metroURL) throws IOException {
        Document doc = Jsoup.connect(metroURL).maxBodySize(0).get();
        JSONObject stations = new JSONObject();
        Elements stationElements = doc.select(".js-metro-stations");
        stationElements.forEach(element -> {
            String stationName = element.text().replaceAll("[0-9]+", "");
            String stationLine = element.attr("data-line");
            JSONArray nameArray = new JSONArray();
            String[] fragments = stationName.split("\\.");
            for (String fragment : fragments) {
                if (!fragment.isEmpty()) {
                    nameArray.add(fragment.trim());
                }
            }
            stations.put(stationLine, nameArray);
        });
        metro.put("stations", stations);
    }
}
