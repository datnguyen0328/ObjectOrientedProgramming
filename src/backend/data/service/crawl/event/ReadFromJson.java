package backend.data.service.crawl.event;

import backend.data.model.event.*;

import java.util.ArrayList;

public interface ReadFromJson {
    public ArrayList<EventInit> loadFromJson(String path);
}
