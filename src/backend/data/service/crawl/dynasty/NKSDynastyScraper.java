package backend.data.service.crawl.dynasty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//import dynasties.object.Dynasty;
//import figures.helpers.HelperFunctions;
import backend.data.model.dynasty.*;
import backend.data.service.decode.HelperFunctions;


public class NKSDynastyScraper {

	public static ArrayList<Dynasty> dynasties = new ArrayList<Dynasty>();

	public static void crawl() {
		try {
			String url = "https://nguoikesu.com";
			Document doc = Jsoup.connect(url).get();

			getDynasties(doc);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void getDynasties(Document doc) {
		String[] years = new String[2];

		Elements test = doc.select("ul.issues");
		Elements dynastiesElement = test.select("li");

		for (Element dynastyElement : dynastiesElement) {
			// reset years
			years[0] = null;
			years[1] = null;

			// title
			Element title = dynastyElement.selectFirst("h3");
			if(title.text().contains("Hồng Bàng"))
					continue;
			System.out.println(title.text());

			// link to detail page
			System.out.println("	Link: " + title.select("a").attr("href"));

			// description
			StringBuilder content = new StringBuilder();
			Elements contentPTags = dynastyElement.select("p");
			for (Element contentPTag : contentPTags) {
				content.append(contentPTag.text() + " ");
			}

			getTime(content.toString(), years);

			System.out.println("	Start years: " + years[0]);
			System.out.println("	End years: " + years[1]);
			System.out.println("	Desc: " + content.toString() + "\n");

			Dynasty myDynasty = new Dynasty(title.text(), HelperFunctions.parseYear(years[0]),
					HelperFunctions.parseYear(years[1]), content.toString());

			dynasties.add(myDynasty);
		}

		HelperFunctions.encodeListToJson(dynasties, "nks_dynasties.json");
		
		
	}
	
	static void getTime(String text, String[] years) {
		int startYear = 0;
		int endYear = 0;
		String startYearTCN = null;
		String endYearTCN = null;

		// Pattern 1: tu (year) den (year)
		Pattern yearPattern = Pattern.compile("từ (\\d+)");
		Matcher yearMatcher = yearPattern.matcher(text);
		if (yearMatcher.find()) {
			if (yearMatcher.group(1) != null) {
				years[0] = yearMatcher.group(1);
			}

			yearPattern = Pattern.compile("đến (\\d+)");
			yearMatcher = yearPattern.matcher(text);

			if (yearMatcher.find()) {
				if (yearMatcher.group(1) != null) {
					years[1] = yearMatcher.group(1);
				}
			}
			return;
		}

		// specail01
		yearPattern = Pattern.compile("1627");
		yearMatcher = yearPattern.matcher(text);
		if (yearMatcher.find()) {
			years[0] = "1627";
			years[1] = "Cuối thế kỉ 18";
			return;
		}

		// special02
		yearPattern = Pattern.compile("3/2/1930");
		yearMatcher = yearPattern.matcher(text);
		if (yearMatcher.find()) {
			years[0] = "1945";
			years[1] = "1975";
			return;
		}
		// special03
		yearPattern = Pattern.compile("3/1/1428");
		yearMatcher = yearPattern.matcher(text);
		if (yearMatcher.find()) {
			years[0] = "1407";
			years[1] = "1427";
			return;
		}

		// Pattern 4: (year)-(year)
		// Check form: year-year
		yearPattern = Pattern.compile("(\\d+) - (\\d+)");
		yearMatcher = yearPattern.matcher(text);
		startYear = 0;
		endYear = 0;
		if (yearMatcher.find()) {
			years[0] = yearMatcher.group(1);
			years[1] = yearMatcher.group(2);
		}
		if (years[0] != null || years[1] != null) {
			return;
		}

		yearPattern = Pattern.compile("(\\d+)-(\\d+)");
		yearMatcher = yearPattern.matcher(text);
		startYear = 0;
		endYear = 0;
		if (yearMatcher.find()) {
			years[0] = yearMatcher.group(1);
			years[1] = yearMatcher.group(2);
		}
		if (years[0] != null || years[1] != null) {
			return;
		}

		// Pattern 2: tu nam (year) or vao nam (year)
		yearPattern = Pattern.compile("từ năm (\\d+)|vào năm (\\d+)");
		yearMatcher = yearPattern.matcher(text);
		if (yearMatcher.find()) {
			if (yearMatcher.group(1) != null) {
				years[0] = yearMatcher.group(1);
				startYear = Integer.parseInt(yearMatcher.group(1));
			}

			if (yearMatcher.group(2) != null) {
				years[0] = yearMatcher.group(2);
				startYear = Integer.parseInt(yearMatcher.group(2));
			}

			startYearTCN = String.valueOf(startYear);

			int yearIndex = text.indexOf(startYearTCN);
			String remainingText = text.substring(yearIndex + startYearTCN.length());

			if (remainingText.contains("TCN"))
				startYearTCN += " TCN";

			// Pattern 3: den nam (year)
			yearPattern = Pattern.compile("đến năm (\\d+)");
			yearMatcher = yearPattern.matcher(text);
			if (yearMatcher.find()) {
				endYear = Integer.parseInt(yearMatcher.group(1));

				endYearTCN = String.valueOf(endYear);
				yearIndex = text.indexOf(endYearTCN);
				remainingText = text.substring(yearIndex + endYearTCN.length());
				if (remainingText.contains("TCN"))
					endYearTCN += " TCN";
			}

			years[0] = startYearTCN;
			years[1] = endYearTCN;
			return;
		}

		// Pattern 3: Ngay (date)/(month)/(year)
		yearPattern = Pattern.compile("\\d{1,2}/\\d{1,2}/(\\d{4})");
		yearMatcher = yearPattern.matcher(text);
		if (yearMatcher.find()) {
			years[0] = yearMatcher.group(1);
			return;
		}

		// Pattern 5: (year)
		yearPattern = Pattern.compile("\\((\\d+)\\)");
		yearMatcher = yearPattern.matcher(text);
		while (yearMatcher.find()) {
			if (years[0] == null) {
				years[0] = yearMatcher.group(1);
			} else {
				endYear = Integer.parseInt(yearMatcher.group(1));
				years[1] = yearMatcher.group(1);
			}
		}

	}
}