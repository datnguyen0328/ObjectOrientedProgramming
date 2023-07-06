package backend.data.service;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.io.BufferedReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

//import objects.Dynasty;
//import objects.Figure;
//import objects.King;

import backend.data.model.*;
import backend.data.model.dynasty.Dynasty;
import backend.data.model.figure.Figure;
import backend.data.service.decode.HelperFunctions;

public class ArrangeFunctions {
	static String filePath1 = "after_dynasties.json";
	static String filePath2 = "after_append.json";
	static ArrayList<Dynasty> dynasties = decodeJsonFileToList(filePath1, Dynasty.class);
	static ArrayList<Figure> figures = HelperFunctions.decodeFromJson(filePath2);
	public static void arrangeFiguresToDynasties() {
		

		int[] counter = new int[40];
		for (int i = 0; i < figures.size(); i++) {
			Figure fig = figures.get(i);
			
			System.out.println();
			
			// if figure has dynasty
			if (!fig.getDynasties().isEmpty()) {
				// loop through each dynasty figure has
				for (String dynastyName : fig.getDynasties())
					// looop through list of refDynasties
					for (int j = dynasties.size() - 1; j >= 0; j--)
						// if equal and count < 20
						if (dynastyName.toLowerCase().equals(dynasties.get(j).getName().toLowerCase())
								&& counter[j] < 20) {
							dynasties.get(j).addFigure(fig);
							counter[j]++;
							break;
						}
			} else {
				// check if found birth, death year
				if (fig.getBornYear() != 0 && fig.getDeathYear() != 0) {
//					System.out.println(HelperFunctions
//							.normalizeString(fig.getName() + ": " + fig.getBornYear() + " -> " + fig.getDeathYear()));

					// avg yesr
					int targetYear = (fig.getBornYear() + fig.getDeathYear()) / 2;

					// loop from end to start and check if counter < 20
					for (int j = dynasties.size() - 1; j >= 0; j--)
						if (targetYear > dynasties.get(j).getStartYear() && counter[j] < 20) {
							dynasties.get(j).addFigure(fig);
							break;
						}
				}
			}
		}
	}

	public static <T> ArrayList<T> decodeJsonFileToList(String filePath, Class<T> elementType) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
//                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Type listType = TypeToken.getParameterized(ArrayList.class, elementType).getType();
        return gson.fromJson(content.toString(), listType);
    }
	

	public static void main(String[] args) {
	   
		System.out.println(figures.size());
		arrangeFiguresToDynasties();
		
		HelperFunctions.encodeListToJson(dynasties, "after_dynasties_sorted.json");
		
	}

}


    

