package backend.data.service.crawl.figure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import backend.data.model.figure.*;
import backend.data.service.decode.HelperFunctions;

public class NKSFigureScraper {

	static int urlCounter = 0;

	static ArrayList<Figure> figures = new ArrayList<Figure>();
	static String[] figureAttributes = new String[6];

	public static void crawl() {

		String url;
		Document doc;
		while (true) {
			// 1450
			if (urlCounter > 5)
				break;
			if (urlCounter == 0)
				url = "https://nguoikesu.com/nhan-vat";
			else
				url = "https://nguoikesu.com/nhan-vat?start=" + urlCounter;

			try {
				doc = Jsoup.connect(url).get();
				getFiguresPage(doc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// encode the list to Json, write to a file and decode back to check
		HelperFunctions.encodeListToJson(figures, "nks_figures.json");
	}

	static void getFiguresPage(Document doc) {
		Elements figureDivs = doc.select("div.com-content-category-blog__item");

		System.out.println("								PAGE: " + urlCounter);

		// loop each figures in a page
		for (Element figure : figureDivs) {
			Element figureNameElement = figure.selectFirst("h2");
			String _figureDetail = figure.select("a").attr("href");

			String figureName = figureNameElement.text();

			if (figureName.startsWith("nhà") || figureName.startsWith("Nhà"))
				continue;
			else
				figureAttributes[0] = figureName;

			accessDetail(_figureDetail);

			// assign id and add to list
			String id = HelperFunctions.normalizeString(figureAttributes[0].toLowerCase()).replaceAll(" ", "");
			if (!figureAttributes[1].equals("Không rõ"))
				id += HelperFunctions.normalizeString(figureAttributes[1].toLowerCase()).replaceAll(" ", "");

			Figure myFigure = new Figure(id, figureAttributes[0], figureAttributes[1],
					HelperFunctions.parseYear(figureAttributes[2]), HelperFunctions.parseYear(figureAttributes[3]),
					figureAttributes[4], figureAttributes[5]);

			// add parent, get dynasty here
			myFigure.setDynasties(HelperFunctions.extractDynasty(figureAttributes[5], FigureScraper.refDynasties));
			myFigure.setParents(HelperFunctions.extractParentsName(figureAttributes[5]));
			
			// add to list & check print
			figures.add(myFigure);
		}
		urlCounter += 5;
	}

	static void accessDetail(String _figureDetail) {

		// set initial attributes to unknown
		for (int i = 1; i < figureAttributes.length; i++)
			figureAttributes[i] = "Không rõ";

		String url = "https://nguoikesu.com" + _figureDetail;
		try {
			Document doc = Jsoup.connect(url).get();

			// get description inside detail page for data scrapping
			StringBuilder desc = new StringBuilder();
			Elements pTags = doc.select("p");

			int count = 0;
			for (Element pTag : pTags) {
				if (count > 5)
					break;
				desc.append(pTag.text() + " ");
				count++;
			}

			// assign description
			figureAttributes[5] = desc.toString();

			// get data from table and paragraph
			Elements rows = doc.select("tr");
			if (rows != null)
				getDataFromTable(rows);

			// get the remaining unknown data from description
			if (pTags.size() != 0) {
				getDataFromParagraph(pTags.get(0), desc.toString());
			}

		} catch (IOException e) {
			return;
		}
	}

	static void getDataFromTable(Elements rows) {

		for (Element row : rows) {
			// each row has 2 columns, title and content
			Element title = row.selectFirst("th");
			Element subDivContent = row.selectFirst("td");

			if (title == null || subDivContent == null)
				continue;

			if (title != null && title.text().equals("Địa lý"))
				return;

			String content = subDivContent != null ? subDivContent.text() : null;

			if (content != null) {
				switch (title.text()) {
				case "Tên đầy đủ": {
					figureAttributes[1] = content;
					break;
				}
				case "Tên khác": {
					figureAttributes[1] = content;
					break;
				}
				case "Biệt danh": {
					figureAttributes[1] = content;
					break;
				}
				case "Sinh": {
					figureAttributes[2] = content;
//					getHome(content);
					extractHome(content);
					break;
				}
				case "Mất": {
					figureAttributes[3] = content;
					break;
				}
				case "Thân phụ": {
					figureAttributes[4] = content;
					break;
				}
				case "Thân mẫu": {
					figureAttributes[5] = content;
					break;
				}
				default:
					break;
				}
			}
		}
	}

	static void getDataFromParagraph(Element div, String desc) {

		// 1st bold text is real name -> 2nd bold text is otherName
		Elements otherNameElements = div.select("b");
		String otherName = "Không rõ";

		if (otherNameElements.size() >= 2 && otherNameElements.get(1) != null)
			otherName = otherNameElements.get(1).text();

		if (otherName.equals("Không rõ"))
			extractOtherName(desc);
		else
			figureAttributes[1] = otherName;

		// get remaining data if not found in table
		if (figureAttributes[2] == "Không rõ" && figureAttributes[3] == "Không rõ")
			getYears(desc);
		if (figureAttributes[4] == "Không rõ")
			extractHome(desc);
	}

	static void extractOtherName(String input) {
		String pattern = "(?:tên thật|hay còn gọi là|hay|hoặc) ([A-ZÀ-Ỹ][a-zà-ỹ]+(?: [A-ZÀ-Ỹ][a-zà-ỹ]+)*)";
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(input);

		if (matcher.find()) {
			figureAttributes[1] = matcher.group(1);
		}
	}

	static void extractHome(String text) {
		//search for cities
		String[] words = text.split("\\s+");
		StringBuilder cityNameBuilder = new StringBuilder();
		for (String word : words) {
			cityNameBuilder.append(word).append(" ");

			for (String city : HelperFunctions.cities) {
				if (cityNameBuilder.toString().contains(city)) {
					figureAttributes[4] = city;
					return;
				}
			}
		}

		String[] keywords = { "làng", "tại", "ở" };

		String pattern = "(?i)(" + String.join("|", keywords) + ")\\s+([A-ZÀ-Ỹ][a-zà-ỹ]+)";
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(text);

		if (matcher.find()) {
			String home = matcher.group(2);
			if (Character.isUpperCase(home.charAt(0))) {
				figureAttributes[4] = home;
				return;
			}
		}
	}

	static void getYears(String str) {
		int birthYear = 0, deathYear = 0;
		String birthYearString, deathYearString;

		Pattern pattern;
		Matcher matcher;

		// check the format: 1990-1991
		pattern = Pattern.compile("(\\d{4})-(\\d{4})");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			birthYearString = matcher.group(1);
			deathYearString = matcher.group(2);

			birthYear = Integer.parseInt(birthYearString);
			deathYear = Integer.parseInt(deathYearString);

			figureAttributes[2] = "" + birthYear;
			figureAttributes[3] = "" + deathYear;

			return;
		}

		pattern = Pattern.compile("\\((.*?)\\)");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			String matchedString = matcher.group(1);
			String[] parts = matchedString.split("-");

			if (parts.length == 2) {
				figureAttributes[2] = parts[0];
				figureAttributes[3] = parts[1];

				return;
			}
		}

		// check the format: sinh nam 1990, mat nam 1990
		pattern = Pattern.compile("(Sinh|sinh) năm (\\d{4})");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			String yearString = matcher.group(2);
			int year = Integer.parseInt(yearString);

			figureAttributes[2] = "" + year;

			return;
		}

		if (str.contains("sinh ngày")) {
			pattern = Pattern.compile("năm (\\d{4})");
			matcher = pattern.matcher(str);
			if (matcher.find()) {
				String year = matcher.group(1);

				figureAttributes[2] = "" + year;

				return;
			}
		}

		pattern = Pattern.compile("(Mất|mất) năm (\\d{4})");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			String yearString = matcher.group(2);
			int year = Integer.parseInt(yearString);

			figureAttributes[3] = "" + year;

			return;
		}

		// check the format: 769 - 860
		birthYear = 0;
		deathYear = 0;
		pattern = Pattern.compile("(\\d{3})-(\\d{3})");
		matcher = pattern.matcher(str);
		if (matcher.find()) {
			birthYearString = matcher.group(1);
			deathYearString = matcher.group(2);

			birthYear = Integer.parseInt(birthYearString);
			deathYear = Integer.parseInt(deathYearString);

			figureAttributes[2] = "" + birthYear;
			figureAttributes[3] = "" + deathYear;

			return;
		}

		// Check the format: "year-?" or "?-year"
		// First, check the 4 digit years
		birthYear = 0;
		deathYear = 0;
		pattern = Pattern.compile("(\\d{1,4}|\\?)-?(\\d{1,4}|\\?)?");
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
		}

		if (birthYear != 0 || deathYear != 0) {
			if (deathYear < 100)
				deathYear = 0;
			if (birthYear < 100)
				birthYear = 0;

			figureAttributes[2] = "" + birthYear;
			figureAttributes[3] = "" + deathYear;

			return;
		}

		// Then check the 3 digit years
		birthYear = 0;
		deathYear = 0;
		pattern = Pattern.compile("(\\d{1,3}|\\?)-?(\\d{1,3}|\\?)?");
		matcher = pattern.matcher(str);
		while (matcher.find()) {
			String match = matcher.group();
			if (match.contains("?")) {
				match = match.replaceAll("\\?", "0");
			}
			if (match.contains("-")) {
				String[] years = match.split("-");
				if (years[0] != null)
					birthYear = Integer.parseInt(years[0]);
				if (years[1] != null)
					deathYear = Integer.parseInt(years[1]);
			} else {
				birthYear = Integer.parseInt(match);
			}
		}

		if (birthYear != 0 || deathYear != 0) {

			figureAttributes[2] = "" + birthYear;
			figureAttributes[3] = "" + deathYear;

			return;
		}
	}
}