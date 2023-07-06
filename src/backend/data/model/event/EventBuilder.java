package backend.data.model.event;

import backend.data.model.dynasty.*;
import backend.data.model.figure.*;
import backend.data.service.decode.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class EventBuilder {
    private ArrayList<Event> eventWrapperArrayList;
    public EventBuilder() {
        this.eventWrapperArrayList = new ArrayList<>();
    }

    public ArrayList<Dynasty> decodeDynasty(String path){
        String currentDir = System.getProperty("user.dir");

        String pathRe = currentDir + "\\" + "OOP_Project" + "\\" + "oop_project" + "\\" + "src" + "\\" + "events" + "\\" + "Resources" + "\\";

        String path1 = pathRe + "dynasty.json";

        JsonParser jsonParser = new JsonParser();
        ArrayList<Dynasty> dynasties = new ArrayList<>();

        try(FileReader reader = new FileReader(path1)) {
            Object obj = jsonParser.parse(reader);
            JsonArray dynastyList = (JsonArray) obj;

            dynastyList.forEach(d ->{
                Dynasty newD = new Dynasty();
                JsonObject e = (JsonObject)d;
                newD.setName(e.get("name").getAsString());
                if(e.get("startYear").getAsString().contains("TCN")){
                    String temp = e.get("startYear").getAsString().split(" ")[0];
                    newD.setStartYear(Integer.parseInt(temp) * -1);
                }else{
                    newD.setStartYear(Integer.parseInt(e.get("startYear").getAsString()));
                }
                if(e.get("endYear") != null){
                    if(e.get("endYear").getAsString().contains("TCN")){
                        String temp = e.get("startYear").getAsString().split(" ")[0];
                        newD.setEndYear(Integer.parseInt(temp) * -1);
                    }else{
                        newD.setEndYear(Integer.parseInt(e.get("endYear").getAsString()));
                    }
                }
                newD.setDesc(e.get("desc").getAsString());
                dynasties.add(newD);
//                System.out.println(d.toString());
            });
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dynasties;
    }
    public ArrayList<Event> decodeEvents(String path) {

        String currentDir = System.getProperty("user.dir");

        String pathRe = currentDir + "\\" + "OOP_Project" + "\\" + "oop_project" + "\\" + "src" + "\\" + "events" + "\\" + "Resources" + "\\";

        String path1 = pathRe + "figures.json";
        String path2 = pathRe + "dynasty.json";

//        ArrayList<Event> resources = new ArrayList<Event>();
        JsonParser jsonParser = new JsonParser();
        try (FileReader reader = new FileReader(path)) {
            //Read JSON file
            Object obj = jsonParser.parse(reader);

            JsonArray eventList = (JsonArray) obj;
//            System.out.println(eventList);
            ArrayList<Figure> figList = HelperFunctions.decodeFromJson(path1);

            //Iterate over event array
            eventList.forEach(e -> {
                JsonObject eventObj = (JsonObject) e;
                Event newEvent = new Event();

//                Event newEvent = new Event();
                newEvent.setId(eventObj.get("id").getAsString());
                newEvent.setName(eventObj.get("name").getAsString());
                newEvent.setTime(eventObj.get("time").getAsString());
                if (eventObj.get("location") != null) {
                    newEvent.setLocation(eventObj.get("location").getAsString());
                }
                if (eventObj.get("description") != null) {
                    newEvent.setDescription(eventObj.get("description").getAsString());
                }


                Dynasty newDynasty = new Dynasty();

                if(eventObj.get("dynasty") != null){
                    String temp = eventObj.get("dynasty").getAsString();
                    ArrayList<Dynasty> dynasties = decodeDynasty(path2);
                    for(Dynasty d: dynasties){
                        if(d.getName().contains(temp)){
                            newDynasty = d;
                            newEvent.setDynastyDetails(newDynasty);
                            break;
                        }
                    }
                }


                if(eventObj.get("relatedFigures") != null){
                    if(!eventObj.get("relatedFigures").getAsJsonArray().isEmpty()){
                        JsonArray figs = (JsonArray) eventObj.get("relatedFigures").getAsJsonArray();
                        // iterate over figs of an event
                        Set<String> figuresStr = new HashSet<>();
                        ArrayList<Figure> relatedFigs = new ArrayList<>();
                        figs.forEach(f ->{
                            String preProcess = f.toString();
                            if(preProcess.length() <= 3){
                                return;
                            }
                            figuresStr.add(f.getAsString());
//                            System.out.println(f.getAsString());
                            // search in figs list
                            String figId = f.getAsString().toLowerCase().replace(" ", "");
                            String temp = Normalizer.normalize(figId, Normalizer.Form.NFD);
                            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
                            temp = pattern.matcher(temp).replaceAll("");
                            temp = temp.replaceAll("đ", "d");
//                            System.out.println("after std: " + temp);
                            for (Figure fg: figList){
                                if(fg.getId().contains(temp)){
                                    relatedFigs.add(fg);
//                                    System.out.println("---------" + fg.toString() + "--------------");
                                    break;
                                }
                            }
                        });
                        if(relatedFigs.size() > 0){
                            newEvent.setRelatedFigsDetails(relatedFigs);
                        }else{
                            newEvent.setRelatedFigure(figuresStr);
                        }
                    }else{
//                        newE.setE(newEvent);
                    }
                }else{
//                    newE.setE(newEvent);
                }
//                System.out.println("------------------------------");
                eventWrapperArrayList.add(newEvent);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventWrapperArrayList;
    }
}
