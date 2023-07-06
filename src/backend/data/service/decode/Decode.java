package backend.data.service.decode;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import backend.data.model.relic.*;
import backend.data.model.figure.*;

public class Decode {
    static String normalizeString(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replace("Đ", "D").replace("đ", "d")
                .replace(" – ", "-").replace("- ", "-").replace(" -", "-").replace("–", "-").replace("—", "-");
    }
    public static ArrayList<Relic> decode(String filePath) {
    	try {
	    	ArrayList<Relic> locals = new ArrayList<Relic>();
	    	Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
	    	Reader reader1 = new FileReader(filePath);
	    	Type listType1 = new TypeToken<List<Relic>>() {
	        }.getType();
	        locals = gson1.fromJson(reader1, listType1);
	        
	        return locals;
    	} catch(Exception e) {
    		e.printStackTrace();
    		return null;
    	}
    }
}