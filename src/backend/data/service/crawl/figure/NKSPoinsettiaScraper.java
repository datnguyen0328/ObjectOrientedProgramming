package backend.data.service.crawl.figure;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import backend.data.model.figure.*;
import backend.data.service.decode.HelperFunctions;

public class NKSPoinsettiaScraper {
	static ArrayList<Poinsettia> poinsettias = new ArrayList<Poinsettia>();

	public static void crawl() {
		
		// Read the JSON file into a string
        String json = HelperFunctions.readFile("merged_figures.json");

        // Create a Gson object with the custom adapter
        Gson gson = new GsonBuilder().create();

        // Convert the JSON to ArrayList<Dynasty>
        Type figureListType = new TypeToken<ArrayList<Figure>>() {}.getType();
        
        ArrayList<Figure> figureList = gson.fromJson(json, figureListType);

		String url = "https://nguoikesu.com/tu-lieu/danh-sach-trang-nguyen-viet-nam";

		try {
			Document doc = Jsoup.connect(url).get();
			getInfo(doc,figureList);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HelperFunctions.encodeListToJson(poinsettias, "poinsettias.json");

	}

	static void getInfo(Document doc,ArrayList<Figure> figureList){
		Element table = doc.selectFirst("table.table.table-bordered");

		if (table != null) {
			Elements rows = table.select("tr");
			for (Element row : rows) {
				Elements cells = row.select("td");
				if (cells.size() >= 6) {
					Element linkElement = cells.get(1).selectFirst("a.annotation");
					if (linkElement != null) {
						String name = linkElement.text();
						if (name.contains("Nguyễn Công Bình")) {
							continue; // Skip elements containing "Bình"
						}
						
//						Figure myFigure = search(figureList, normalizeString(name));
						Figure myFigure = search(figureList, name);
						if(myFigure == null) {
							System.out.println(name + " NOT FOUND");
							continue;
						}

						String home = cells.get(3).text();
						String trangYear = cells.get(4).text();
						String king = cells.get(5).text();
//						String id = HelperFunctions.normalizeString(myFigure.getName().toLowerCase()).replaceAll(" ", "") ;
//				            if(!myFigure.getOtherName().equals("Không rõ"))
//				            	id+= HelperFunctions.normalizeString(myFigure.getOtherName().toLowerCase()).replaceAll(" ", "").replaceAll("-", "") ;
//				            System.out.println(id);
//						String annotation = linkElement.selectFirst("a").attr("title");
						ArrayList<String> dynies = myFigure.getDynasties();
						Poinsettia myPoinsettia = new Poinsettia(
								myFigure.getId(),
								myFigure.getName(),
								myFigure.getOtherName(),
								myFigure.getBornYear(), 
								myFigure.getDeathYear(),
								myFigure.getParents(),
								dynies,
								home,
								king,
								myFigure.getDesc(),
								trangYear);
						
						poinsettias.add(myPoinsettia);
					}
				}
			}
		}
		
		for(Poinsettia p: poinsettias) {
			HelperFunctions.prtPointessiaAttributeas(p);
		}
		
		//HelperFunctions.encodeListToJson(poinsettias,"after_poinsettias.json");

	}

	static void getYears(String str) {
		int birthYear = 0, deathYear = 0;
		String birthYearString, deathYearString;

//		Special pattern

		Pattern pattern = Pattern.compile("11-2-1050-?");
		Matcher matcher = pattern.matcher(str);

		if (matcher.find()) {
			birthYear = 1050;
			System.out.println("	SINH (1): " + birthYear);
			System.out.println("	MAT (1): " + deathYear);

			return;
		}

		// check the pattern: 1990-1991

		pattern = Pattern.compile("(\\d+)-(\\d+)");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			birthYearString = matcher.group(1);
			deathYearString = matcher.group(2);

			birthYear = Integer.parseInt(birthYearString);
			deathYear = Integer.parseInt(deathYearString);

			System.out.println("	SINH (2): " + birthYear);
			System.out.println("	MAT (2): " + deathYear);
			return;
		}

		// Check the format: "year-?" or "?-year"

		birthYear = 0;
		deathYear = 0;
		pattern = Pattern.compile("(\\d+|\\?)-?(\\d+|\\?)");
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			String match = matcher.group();
			if (match.contains("?")) {
				match = match.replaceAll("\\?", "0");
			}
			if (match.contains("-")) {
				String[] years = match.split("-");
				if (years.length >= 2) {
					if (years[0] != null)
						birthYear = Integer.parseInt(years[0]);
					if (years[1] != null)
						deathYear = Integer.parseInt(years[1]);
				}
			} else {
				birthYear = Integer.parseInt(match);
			}

			System.out.println("	SINH (3): " + birthYear);
			System.out.println("	MAT (3): " + deathYear);
			return;
		}

	}
	
	static Figure search(ArrayList<Figure> sortedList, String target) {

		for (Figure figure: sortedList)
			if (figure.getName().equals(target))
				return figure;
					

	    // Target string not found
	    return null;
	}
}