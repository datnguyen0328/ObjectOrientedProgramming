package backend.data.service.decode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

//import objects.Dynasty;
//import objects.Figure;
//import objects.King;
//import objects.Poinsettia;
import backend.data.model.*;
import backend.data.model.dynasty.Dynasty;
import backend.data.model.event.Event;
import backend.data.model.figure.Figure;
import backend.data.model.figure.King;
import backend.data.model.figure.Poinsettia;

public class HelperFunctions {
	
	public static List<String> cities = Arrays.asList("An Giang", "Bạc Liêu", "Bắc Giang", "Bắc Kạn",
			"Bắc Ninh", "Bến Tre", "Bình Dương", "Bình Định", "Bình Phước", "Bình Thuận", "Cà Mau", "Cao Bằng",
			"Cần Thơ", "Đà Nẵng", "Dak Lak", "Dak Nông", "Điện Biên", "Đồng Nai", "Đồng Tháp", "Gia Lai", "Hà Giang",
			"Hà Nam", "Hà Nội", "Hà Tĩnh", "Hải Dương", "Hải Phòng", "Hậu Giang", "Hoà Bình", "Huế", "Hưng Yên",
			"Hồ Chí Minh", "Khánh Hoà", "Kiên Giang", "Kon Tum", "Lai Châu", "Lạng Sơn", "Lào Cai", "Lâm Đồng",
			"Long An", "Nam Định", "Nghệ An", "Ninh Bình", "Ninh Thuận", "Phú Thọ", "Phú Yên", "Quảng Bình",
			"Quảng Nam", "Quảng Ngãi", "Quảng Ninh", "Quảng Trị", "Sóc Trăng", "Sơn La", "Tây Ninh", "Thái Bình",
			"Thái Nguyên", "Thanh Hoá", "Thừa Thiên Huế", "Tiền Giang", "Trà Vinh", "Tuyên Quang", "Vĩnh Long",
			"Vĩnh Phúc", "Yên Bái", "Thừa Thiên", "Thăng Long");

	public static String normalizeString(String s) {
		return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replace("Đ", "D").replace("đ", "d")
				.replace(" – ", "-").replace("- ", "-").replace(" -", "-").replace("–", "-");
	}

	public static void prtFigureAttributes(Figure myFigure) {
		System.out.println(myFigure.getName());
		System.out.println(myFigure.getId());
		System.out.println("	Ten khac: " + myFigure.getOtherName());
		System.out.println("	Sinh: " + myFigure.getBornYear());
		System.out.println("	Mat: " + myFigure.getDeathYear());
		System.out.println("	Cha/Me: " + myFigure.getParents());
//		System.out.println("	Trieu dai: " + myFigure.getDynasty().getName());
		System.out.println("	Trieu dai: " + myFigure.getDynasties());

		System.out.println("	Que quan: " + myFigure.getHome());
		System.out.println("	Mo ta: " + myFigure.getDesc());
	}

	public static void prtPointessiaAttributeas(Poinsettia myFigure) {
		System.out.println(myFigure.getName());
		System.out.println("	Ten khac: " + myFigure.getOtherName());
		System.out.println("	Sinh: " + myFigure.getBornYear());
		System.out.println("	Mat: " + myFigure.getDeathYear());
		System.out.println("	Cha/Me: " + myFigure.getParents());
		System.out.println("	Trieu dai: " + myFigure.getDynasties());
		System.out.println("	Que quan: " + myFigure.getHome());
		System.out.println("	Trieu vua: " + myFigure.getKingYear());
		System.out.println("	Nam do trang nguyen: " + myFigure.getGraduatedYear());
		System.out.println("	Mo ta: " + myFigure.getDesc());
	}

	public static void prtDynasty(Dynasty dynasty) {
		System.out.println(dynasty.getName());
		System.out.println("	" + dynasty.getStartYear());
		System.out.println("	" + dynasty.getEndYear());
		System.out.println("	" + dynasty.getDesc());
	}

	public static List<Object> searchSortedDuplicates(List<Object> sortedList, String target) {
		List<Object> resSObjs = new ArrayList<>();
		Object sample = sortedList.get(0);

		int left = 0;
		int right = sortedList.size() - 1;

		while (left <= right) {
			int mid = left + (right - left) / 2;

			if (sample instanceof Figure) {
				Figure tmpFigure = (Figure) sortedList.get(mid);
				if (normalizeString(tmpFigure.getName()).compareTo(normalizeString(target)) == 0) {
					resSObjs.add(tmpFigure);

					int index = mid - 1;
					Figure nextFigure = (Figure) sortedList.get(index);
					while (index >= 0
							&& normalizeString(nextFigure.getName()).compareTo(normalizeString(target)) == 0) {
						resSObjs.add(nextFigure);
						index--;
						nextFigure = (Figure) sortedList.get(index);
					}

					index = mid + 1;
					nextFigure = (Figure) sortedList.get(index);
					while (index < sortedList.size()
							&& normalizeString(nextFigure.getName()).compareTo(normalizeString(target)) == 0) {
						resSObjs.add(nextFigure);
						index++;
						nextFigure = (Figure) sortedList.get(index);
					}

					return resSObjs;
				} else if (normalizeString(((Figure) sortedList.get(mid)).getName())
						.compareTo(normalizeString(target)) < 0) {
					left = mid + 1;
				} else {
					right = mid - 1;
				}
			}
			// if list is long then add instance
		}

		// No match found
		return resSObjs;
	}

	public static List<Figure> searchFigureLinear(List<Figure> sortedList, String name) {
		List<Figure> resList = new ArrayList<>();
		List<Figure> resListNormalized = new ArrayList<>();
		for (Figure figure : sortedList)
			if (figure.getName().equals(name))
				resList.add(figure);
			else if (normalizeString(figure.getName()).contains(normalizeString(name)))
				resListNormalized.add(figure);
		if (!resList.isEmpty())
			return resList;
		return resListNormalized;
	}

	public static List<Dynasty> searchDynastyLinear(List<Dynasty> sortedList, String name) {
		List<Dynasty> resList = new ArrayList<>();
		for (Dynasty dynasty : sortedList) {
			String dName = normalizeString(dynasty.getName());
			if (dName.equals(normalizeString(name)) || dName.contains(normalizeString(name)))
				resList.add(dynasty);
		}
		return resList;
	}

	public static int parseYear(String _input) {
		if (_input == null || _input == "Không rõ")
			return 0;

		String input = normalizeString(_input);
		// Regular expression to match one or more digits
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(input);

		int largestNumber = 0; // Default value if no numbers are found

		while (matcher.find()) {
			int number = Integer.parseInt(matcher.group());
			if (number > largestNumber) {
				largestNumber = number;
			}
		}

		if (input.contains("TCN"))
			largestNumber *= -1;

		return largestNumber;
	}

	public static ArrayList<String> extractParentsName(String input) {
		ArrayList<String> names = new ArrayList<>();

		String pattern = "(?:con của) ([A-ZÀ-Ỹ][a-zà-ỹ]+(?: [A-ZÀ-Ỹ][a-zà-ỹ]+)*) (?:và) ([A-ZÀ-Ỹ][a-zà-ỹ]+(?: [A-ZÀ-Ỹ][a-zà-ỹ]+)*)";
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(input);

		if (matcher.find()) {
			String name1 = matcher.group(1).trim();
			names.add(name1);
			String name2 = matcher.group(2).trim();
			names.add(name2);
			return names;
		}

		pattern = "(?:cha là) ([A-ZÀ-Ỹ][a-zà-ỹ]+(?: [A-ZÀ-Ỹ][a-zà-ỹ]+)*), (?:mẹ là) ([A-ZÀ-Ỹ][a-zà-ỹ]+(?: [A-ZÀ-Ỹ][a-zà-ỹ]+)*)";
		regex = Pattern.compile(pattern);
		matcher = regex.matcher(input);

		if (matcher.find()) {
			String name1 = matcher.group(1).trim();
			names.add(name1);
			String name2 = matcher.group(2).trim();
			names.add(name2);
			return names;
		}

		pattern = "(?:là con trai của|là con gái của|con của|con thứ \\d+ của|cha là|mẹ là) ([A-ZÀ-Ỹ][a-zà-ỹ]+(?: [A-ZÀ-Ỹ][a-zà-ỹ]+)*)";
		regex = Pattern.compile(pattern);
		matcher = regex.matcher(input);

		if (matcher.find()) {
			String name = matcher.group(1).trim();
			names.add(name);
		}

		return names;
	}

	public static String readFile(String filePath) {
		StringBuilder content = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				content.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public static ArrayList<String> extractDynasty(String input, ArrayList<String> refDynastiesList) {
		ArrayList<String> resDynasties = new ArrayList<>();
		if (input.contains("vua Lê chúa Trịnh")) {
			for (String dynasty : refDynastiesList) {
				if (dynasty.equals("Trịnh Nguyễn Phân Tranh"))
					resDynasties.add(dynasty);
			}
		}
		String pattern = "(?:Nhà|nhà|triều đại|triều) ([A-ZÀ-Ỹ][a-zà-ỹ]+(?: [A-ZÀ-Ỹ][a-zà-ỹ]+)*)";
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(input);
		if (matcher.find()) {
			if (!(matcher.group(1).contains("đình") || matcher.group(1).contains("nho")
					|| matcher.group(1).contains("Nho") || matcher.group(1).contains("đại"))) {
				String targetName = matcher.group(1).toLowerCase();
				boolean found = false;
				for (String dynasty : refDynastiesList)
					if (dynasty.toLowerCase().contains(targetName)) {
						found = true;
						resDynasties.add(dynasty);
					}
				if (!found)
					System.out.println("Unknown: " + HelperFunctions.normalizeString(targetName));
			}
		}
		return resDynasties;
	}
	
//	public static ArrayList<String> extractDynasty(String input, ArrayList<Dynasty> refDynastiesList) {
//		ArrayList<String> resDynasties = new ArrayList<>();
//		if (input.contains("vua Lê chúa Trịnh")) {
//			for (Dynasty dynasty : refDynastiesList) {
//				if (dynasty.getName().equals("Trịnh Nguyễn Phân Tranh"))
//					resDynasties.add(dynasty.getName());
//			}
//		}
//		String pattern = "(?:Nhà|nhà|triều đại|triều) ([A-ZÀ-Ỹ][a-zà-ỹ]+(?: [A-ZÀ-Ỹ][a-zà-ỹ]+)*)";
//		Pattern regex = Pattern.compile(pattern);
//		Matcher matcher = regex.matcher(input);
//		if (matcher.find()) {
//			if (!(matcher.group(1).contains("đình") || matcher.group(1).contains("nho")
//					|| matcher.group(1).contains("Nho") || matcher.group(1).contains("đại"))) {
//				String targetName = matcher.group(1).toLowerCase();
//				boolean found = false;
//				for (Dynasty dynasty : refDynastiesList)
//					if (dynasty.getName().toLowerCase().contains(targetName)) {
//						found = true;
//						resDynasties.add(dynasty.getName());
//					}
//				if (!found)
//					System.out.println("Unknown: " + HelperFunctions.normalizeString(targetName));
//			}
//		}
//		return resDynasties;
//	}

	public static ArrayList<Figure> decodeFromJson(String filepath) {
		ArrayList<Figure> resList = new ArrayList<>();
		JsonParser jParser = new JsonParser();
		try (FileReader reader = new FileReader(filepath)) {
			Object obj = jParser.parse(reader);
			JsonArray figList = (JsonArray) obj;
			figList.forEach(e -> {
				JsonObject figObject = (JsonObject) e;
				if (figObject.get("eraName") != null) {
					King tmpKing = new King();
					tmpKing.setId(figObject.get("id").getAsString());
					tmpKing.setBornYear(figObject.get("bornYear").getAsInt());
					tmpKing.setName(figObject.get("name").getAsString());
					tmpKing.setRegnalYear(figObject.get("regnalYear").getAsString());
					tmpKing.setDesc(figObject.get("desc").getAsString());
					tmpKing.setPosthumousName(figObject.get("posthumousName").getAsString());
					tmpKing.setEraName(figObject.get("eraName").getAsString());
					tmpKing.setHeirApparent(figObject.get("heirApparent").getAsString());
					JsonArray parentsArray = figObject.getAsJsonArray("parents");
					ArrayList<String> parents = new ArrayList<>();
					for (JsonElement parent : parentsArray) {
						parents.add(parent.getAsString());
					}
					tmpKing.setParents(parents);
					JsonArray dynastiesArray = figObject.getAsJsonArray("dynasties");
					ArrayList<String> dynasties = new ArrayList<>();
					for (JsonElement dynasty : dynastiesArray) {
						dynasties.add(dynasty.getAsString());
					}
					tmpKing.setDynasties(dynasties);
					resList.add(tmpKing);
				} else if (figObject.get("graduatedYear") != null) {
					Poinsettia poin = new Poinsettia();
					poin.setId(figObject.get("id").getAsString());
					poin.setBornYear(figObject.get("bornYear").getAsInt());
					poin.setName(figObject.get("name").getAsString());
					poin.setHome(figObject.get("home").getAsString());
					poin.setKingYear(figObject.get("king").getAsString());
					poin.setDesc(figObject.get("desc").getAsString());
					poin.setGraduatedYear(figObject.get("graduatedYear").getAsString());
					JsonArray parentsArray = figObject.getAsJsonArray("parents");
					ArrayList<String> parents = new ArrayList<>();
					for (JsonElement parent : parentsArray) {
						parents.add(parent.getAsString());
					}
					poin.setParents(parents);
					JsonArray dynastiesArray = figObject.getAsJsonArray("dynasties");
					ArrayList<String> dynasties = new ArrayList<>();
					for (JsonElement dynasty : dynastiesArray) {
						dynasties.add(dynasty.getAsString());
					}
					poin.setDynasties(dynasties);
					resList.add(poin);
				} else {
					Figure newFig = new Figure();
					newFig.setId(figObject.get("id").getAsString());
					newFig.setBornYear(figObject.get("bornYear").getAsInt());
					newFig.setName(figObject.get("name").getAsString());
					newFig.setOtherName(figObject.get("otherName").getAsString());
					newFig.setDeathYear(figObject.get("deathYear").getAsInt());
					newFig.setHome(figObject.get("home").getAsString());
					newFig.setDesc(figObject.get("desc").getAsString());
					JsonArray parentsArray = figObject.getAsJsonArray("parents");
					ArrayList<String> parents = new ArrayList<>();
					for (JsonElement parent : parentsArray) {
						parents.add(parent.getAsString());
					}
					newFig.setParents(parents);
					JsonArray dynastiesArray = figObject.getAsJsonArray("dynasties");
					ArrayList<String> dynasties = new ArrayList<>();
					for (JsonElement dynasty : dynastiesArray) {
						dynasties.add(dynasty.getAsString());
					}
					newFig.setDynasties(dynasties);
					resList.add(newFig);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resList;
	}
	
	public static ArrayList<Dynasty> decodeDynastiesFromJson(String filePath) {
        ArrayList<Dynasty> dynastyList = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new GsonBuilder().create();
            Type dynastyListType = new TypeToken<ArrayList<Dynasty>>(){}.getType();
            dynastyList = gson.fromJson(reader, dynastyListType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dynastyList;
    }
	
	public static ArrayList<String> decodeDynastyNamesFromJson(String filepath) {
		ArrayList<String> resList = new ArrayList<>();
		JsonParser jParser = new JsonParser();
		try (FileReader reader = new FileReader(filepath)) {
			Object obj = jParser.parse(reader);
			JsonArray dynList = (JsonArray) obj;
			dynList.forEach(e -> {
				JsonObject dynObject = (JsonObject) e;
				if (dynObject.get("name") != null) {
					resList.add(dynObject.get("name").getAsString());
				}else {
					System.out.println("ERROR");
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resList;
	}
	
	public static ArrayList<Event> decodeEventFromJson(String filePath) {
        ArrayList<Event> eventList = new ArrayList<>();

        try (FileReader reader = new FileReader(filePath)) {
            Gson gson = new GsonBuilder().create();
            Type eventListType = new TypeToken<ArrayList<Event>>(){}.getType();
            eventList = gson.fromJson(reader, eventListType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventList;
    }

	public static <T> void encodeListToJson(ArrayList<T> list, String filePath) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(list);
		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(json);
			writer.close();

            System.out.println("JSON file created successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
