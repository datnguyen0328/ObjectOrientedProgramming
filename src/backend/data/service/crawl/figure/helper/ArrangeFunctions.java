package backend.data.service.crawl.figure.helper;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.io.BufferedReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import backend.data.constant.Constant;
import backend.data.model.dynasty.*;
import backend.data.model.figure.*;
import backend.data.service.decode.HelperFunctions;

public class ArrangeFunctions {
	static ArrayList<Dynasty> dynasties = HelperFunctions.decodeDynastiesFromJson(Constant.DYNASTY_FILE_NAME);
	static ArrayList<Figure> figures = HelperFunctions.decodeFromJson(Constant.FIGURE_FILE_NAME);
	
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
						if (targetYear > dynasties.get(j).getStartYear() && targetYear < dynasties.get(j).getEndYear() && counter[j] < 20) {
							dynasties.get(j).addFigure(fig);
							break;
						}
				}
			}
		}
		
		HelperFunctions.encodeListToJson(dynasties, "arranged_figures_dynasties.json");
	}

}


    

