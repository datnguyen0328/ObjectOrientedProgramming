package backend.data.service.crawl.event;

import backend.data.constant.Constant;
import backend.data.model.event.*;
import backend.data.service.decode.HelperFunctions;

import java.io.IOException;
import java.util.ArrayList;

public class Scraper {
    public static void main(String[] args) throws IOException {
        ArrayList<Event> eList = HelperFunctions.decodeEventFromJson(Constant.EVENT_FILE_NAME);
        System.out.println("***********************************");
        for(Event x: eList){
            System.out.println(x.toString());
        }
        System.out.println(eList.size());

    }
}