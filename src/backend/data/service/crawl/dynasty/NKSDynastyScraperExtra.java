package backend.data.service.crawl.dynasty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import backend.data.model.dynasty.*;
import backend.data.service.decode.HelperFunctions;

public class NKSDynastyScraperExtra {
	static int urlCounter = 0;
	static int figureCounter = 0;
	static int parentCounter = 0;
	static int homeCounter = 0;

	static ArrayList<Dynasty> extraList = new ArrayList<Dynasty>();
	static String[] dynastyAttributes = new String[3];

	public static void crawl() {

		try {
			String url;
			Document doc;
			while (true) {
				if (urlCounter < 935) {
					urlCounter += 5;
					continue;
				} else if (urlCounter > 965)
					break;

				if (urlCounter == 0)
					url = "https://nguoikesu.com/nhan-vat";
				else
					url = "https://nguoikesu.com/nhan-vat?start=" + urlCounter;
				doc = Jsoup.connect(url).get();
				getDynastyPage(doc);
			}

			HelperFunctions.encodeListToJson(extraList, "nks_dynasties_extra.json");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static void getDynastyPage(Document doc) {
		System.out.println("								" + urlCounter);

		Elements dynasties = doc.select("div.com-content-category-blog__item");

		// loop each dynasties in a page
		for (Element dynasty : dynasties) {
			Element nameElement = dynasty.selectFirst("h2");

			String name = (nameElement.text());
			String detailLink = nameElement.select("a").attr("href");

			if (name.startsWith("nhà") || name.startsWith("Nhà")) {
//				System.out.println("TRIEU DAI: " + name);
				if (dynastyDetail(detailLink)) {

					
					// after get data, add to list
					Dynasty myDynasty = new Dynasty(
							name,
							HelperFunctions.parseYear(dynastyAttributes[0]),
							HelperFunctions.parseYear(dynastyAttributes[1]),
							dynastyAttributes[2]);
					extraList.add(myDynasty);
				}
			} else
				continue;
		}
		urlCounter += 5;
	}

	static Boolean dynastyDetail(String link) {
		String url = "https://nguoikesu.com" + link;
//		System.out.println("	" + url);
		try {
			Document doc = Jsoup.connect(url).get();

			getTime(doc);
			return getCountry(doc);

		} catch (IOException e) {
			System.out.println("			CANNOT GET DOC");
			return false;
		}
	}

	static void getTime(Document doc) {
		Boolean alreadyExtractYear = false;
		dynastyAttributes[0] = null;
		dynastyAttributes[1] = null;
		dynastyAttributes[2] = null;

		Element infobox = doc.selectFirst("div.infobox");
		if (infobox != null) {
			Elements trTags = infobox.select("tr");

			StringBuilder sb = new StringBuilder();
			for (Element trTag : trTags) {
				sb.append("\n		" + (trTag.text()));
			}

			alreadyExtractYear = extractYearTable(sb.toString());
		}
		
		//get description
		Elements pTags = doc.select("p");
		StringBuilder sb = new StringBuilder();
		
		int pCounter = 0;
		for (Element pTag : pTags) {
			pCounter++;
			if(pCounter > 3) break;
			sb.append("\n	" + (pTag.text()));
		}
		dynastyAttributes[2] = sb.toString();
		
		//get years if there's no table
		if (!alreadyExtractYear)
			extractYearTable(sb.toString());
	}

	static Boolean extractYearTable(String text) {
		Pattern pattern = Pattern.compile("(\\d+)( TCN)?-(\\d+)( TCN)?");
		Matcher matcher = pattern.matcher(text);

		String startYear = null;
		String endYear = null;

		while (matcher.find()) {
			startYear = matcher.group(1);
			endYear = matcher.group(3);

			if (matcher.group(2) != null) {
				startYear += " TCN";
			}
			if (matcher.group(4) != null) {
				endYear += " TCN";
			}

			break; // Assuming we only need the first occurrence
		}
		if (startYear != null && endYear != null) {
			dynastyAttributes[0] = startYear;
			dynastyAttributes[1] = endYear;
			return true;
		}
		return false;
	}

	static Boolean getCountry(Document doc) {
		Element containerDiv = doc.selectFirst("div.com-content-article__body");
		Elements paragraphs = containerDiv.select("p");
		String[] paragraphTexts = new String[3]; // Array to store the paragraph texts

		int count = 0;

		for (Element paragraph : paragraphs) {
			if (paragraph.parent() == containerDiv && count < 3) {
				String text = (paragraph.text());
				paragraphTexts[count] = text;
				count++;
			}
		}

		for (int i = 0; i < paragraphTexts.length; i++) {
			String text = paragraphTexts[i];

			if (text.toLowerCase().contains("trung quốc"))
				return false;

		}
		return true;
	}

	static void prtDynasty(Dynasty dynasty) {
		System.out.println(dynasty.getName());
		System.out.println("	" + dynasty.getStartYear());
		System.out.println("	" + dynasty.getEndYear());
		System.out.println("	" + dynasty.getDesc());
	}
}