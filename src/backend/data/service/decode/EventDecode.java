package backend.data.service.decode;

import backend.data.model.event.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EventDecode {

    public static ArrayList<EventInit> decode(String path) {
        ArrayList<EventInit> resources = new ArrayList<EventInit>();
        JsonParser jsonParser = new JsonParser();
        try (FileReader reader = new FileReader(path)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JsonArray eventList = (JsonArray) obj;

            //Iterate over EventInit array
            eventList.forEach(e -> {
                JsonObject eventObj = (JsonObject) e;
                EventInit newEvent = new EventInit();
                newEvent.setName(eventObj.get("name").getAsString());
                newEvent.setTime(eventObj.get("time").getAsString());
                if (eventObj.get("location") != null) {
                    newEvent.setLocation(eventObj.get("location").getAsString());
                }
                if (eventObj.get("description") != null) {
                    newEvent.setDescription(eventObj.get("description").getAsString());
                }

                newEvent.setId(eventObj.get("id").getAsString());
                resources.add(newEvent);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resources;
    }
}
