package backend.data.service.crawl.figure;

import java.io.IOException;
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
import java.lang.reflect.Type;

import backend.data.model.figure.*;
import backend.data.service.decode.HelperFunctions;

public class VSFigureScraper {
	static int figureCount = 0;
	static int homeCount = 0;
	static int dynastyCount = 0;
	static int parentCount = 0;
	static int bitrhDeathCount = 0;

	static int urlCounter = 0;
	
	
	public static ArrayList<Figure> figures = new ArrayList<Figure>();
	static String[] figureAttributes = new String[9];
	
	public static void crawl() { 
		String url;
		Document doc;
		while (true) {
			
			if (urlCounter > 119)
				break;
//			urlCounter=110;
			if (urlCounter == 0)
				url = "https://vansu.vn/viet-nam/viet-nam-nhan-vat";
			else
				url = "https://vansu.vn/viet-nam/viet-nam-nhan-vat?page=" + urlCounter;
//			urlCounter=1120;
			try {
				doc = Jsoup.connect(url).get();
				getFiguresPage(doc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		HelperFunctions.encodeListToJson(figures,"vs_figures.json");
	}

	static void getFiguresPage(Document doc) {
	    Element table = doc.selectFirst("table.ui.selectable.celled.table");
	    
	    System.out.println("			PAGE: " + urlCounter);

	    Elements rows = table.select("tr");
	    for (Element row : rows) {
	        // Get the data from each cell in the row
	        Elements cells = row.select("td");
	        if (!cells.isEmpty()) {
	            figureAttributes[0] = cells.get(0).text().trim();
	            Element cell = cells.get(1);
	            String cellContent = cell.html().replaceAll("<br>", "&").replaceAll("lần 1", "lần I").replaceAll("lần 2", "lần II").replaceAll("lần lần 3", "lần III").replaceAll("Triều", "").replaceAll(", Triệu", "").replaceAll("độc lập", ""); // Replace <br> with a &
	            System.out.println(Jsoup.parse(cellContent).text().trim());
	            ArrayList<String> dynasties = new ArrayList<>();
	            
	            // Separate Dynasties as Strings
	            String dynastyString = findDynasty(Jsoup.parse(cellContent).text().trim());
	            // Save Dynasties in arrayList
	            dynasties = extractDynasty(dynastyString);
	           
	            figureAttributes[4] = cells.get(2).text().trim();
	            //figureAttributes[1] = "Không rõ";
	            Element firstCell = cells.first();
	            Element link = firstCell.selectFirst("a");
	            if (link != null) {
	                String href = link.attr("href");
	                accessDetail(href);
	            }

	             System.out.println("\n	Figure COUNT "+figureCount);
	            
	            figureCount++;
	            
	            String id = HelperFunctions.normalizeString(figureAttributes[0].toLowerCase()).replaceAll(" ", "") ;
	            if(!figureAttributes[1].equals("Không rõ"))
	            	id+= HelperFunctions.normalizeString(figureAttributes[1].toLowerCase()).replaceAll(" ", "").replaceAll("-", "") ;
	            System.out.println(id);

	            // Create a new Figure object with the extracted data
	            Figure myFigure = new Figure(id,figureAttributes[0], figureAttributes[1], HelperFunctions.parseYear(figureAttributes[2]) ,
	            		HelperFunctions.parseYear(figureAttributes[3]),HelperFunctions.extractParentsName(figureAttributes[5]),checkDynasties(dynasties) , figureAttributes[4], figureAttributes[5]);
	            figures.add(myFigure);
	            HelperFunctions.prtFigureAttributes(myFigure);
	        }
	    }

	    urlCounter ++;
	}
	
	private static void accessDetail(String tail) {
				// set initial attributes to unknown
		for (int i = 1; i <= 3; i++)
			figureAttributes[i] = "Không rõ";
		//figureAttributes[8] = "Không rõ";
		
        String url = "https://vansu.vn" + tail;
        try {
        	boolean foundName = false;
    		boolean foundBirth = false;

            Document doc = Jsoup.connect(url).get();
            StringBuilder desc = new StringBuilder();
            Element table = doc.selectFirst("table.ui.selectable.celled.table");
            if (table != null) {
            	 Elements rows = table.select("tr");
            	// get other name and birth/death
            	 if (rows.size() >= 2) {
            		 for (int i=0;i<2;i++) {
            			 Element firstRow = rows.get(i);
            			 Element firstCell = firstRow.selectFirst("td");
            			 String firstCellContent = firstCell.text().trim();
            			 Element secondCell;
//                         String secondCellContent = secondCell.text().trim();
                         
            			 if (firstCellContent.equals("Tên khác")) {
            				 secondCell = firstRow.select("td").get(1);
            				 figureAttributes[1] = secondCell.text().trim();
            				 foundName=true;
                             }

                         if (firstCellContent.equals("Năm sinh")) {
                        	 secondCell = firstRow.select("td").get(1);
                        	 extractBirth(secondCell.text().trim());
                        	 foundBirth=true;
                         }
            			 
            		 }

                 }
            	 // get description
                if (!rows.isEmpty()) {
                    Element lastRow = rows.last();
                    Elements cells = lastRow.select("td");

                    for (Element cell : cells) {
                        desc.append(cell.text().trim());
                    }
                }
                
                if (foundName==false)
                	extractOtherName(desc.toString());
                
                //assign description
                figureAttributes[5] = desc.toString();

            }
		} catch (IOException e) {
			return;
		}
	}
	
	//Save Dynasty to String
	private static String findDynasty(String input) {
		String pattern = "(?<=^-\\s).*?(?=\\s*-\\s\\(|\\s*&\\s*$)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);
        StringBuilder builder = new StringBuilder();
        while (matcher.find()) {
            String section = matcher.group().trim();
            section = section.replaceAll("\\(.*?\\)", "").trim();
            section = section.replace(" -", "");

            String[] parts = section.split("&");
            for (int i = 0; i < parts.length; i++) {
                if (i > 0) {
                    builder.append(";");
                }
                builder.append(parts[i].trim());
            }
        }
        return builder.toString();
	}
	
	//Extract dynasties to arraylist
	private static ArrayList<String> extractDynasty(String input) {
	    ArrayList<String> dynasties = new ArrayList<>();
	    String[] dynastyNames = input.split(";");

	    for (String dynastyName : dynastyNames) {
	        dynasties.add(dynastyName.trim());
	    }

	    return dynasties;
	}

	
	static ArrayList<String> checkDynasties(ArrayList<String> inputs) {
		ArrayList<String> resDynasties = new ArrayList<>();

        for (String input : inputs) {
            for (String dynasty : FigureScraper.refDynasties) {
                String targetName = dynasty.toLowerCase();
                if (targetName.contains(input.toLowerCase())) {
                    resDynasties.add(dynasty);
                }
            }
        }

        return resDynasties;
    }
	
	


	
	private static void extractBirth(String content) {
		if(content.contains("Trcn")) {
			figureAttributes[3]="180 TCN";
		}
		else {
			String[] parts = content.split("-");

			if (parts.length == 2) {
				String firstYear = parts[0].trim();
				String secondYear = parts[1].trim();

				// Check if the first year contains ellipsis (…)
				if (firstYear.endsWith("…")) {
					figureAttributes[3] = extractYear(firstYear);
				} else {
					figureAttributes[2] = extractYear(firstYear);
				}

				// Check if the second year contains ellipsis (…)
				if (secondYear.startsWith("…")) {
					figureAttributes[2] = extractYear(secondYear);
				} else {
					figureAttributes[3] = extractYear(secondYear);
				}
			}
		}

	}
	
	static void extractOtherName(String input) {
        String pattern = "(?:tên thật là|pháp danh|tự là) ([A-ZÀ-Ỹ][a-zà-ỹ]+(?: [A-ZÀ-Ỹ][a-zà-ỹ]+)*)";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);

        if (matcher.find()) {
	        	if (!figureAttributes[0].contains(matcher.group(1)))
	        		figureAttributes[1] = matcher.group(1);
        }
    }

	private static String extractYear(String text) {
	    String year = text.replaceAll("\\D+", "");
	    return year.isEmpty() ? "không rõ" : year;
	}
	
	

	

}