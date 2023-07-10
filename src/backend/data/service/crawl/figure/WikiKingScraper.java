package backend.data.service.crawl.figure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import backend.data.model.figure.*;
import backend.data.service.decode.HelperFunctions;

public class WikiKingScraper {
	static String[] kingAttributes = new String[14];
	static ArrayList<King> kings = new ArrayList<King>();

	Gson gson = new GsonBuilder().create();

	public static void crawl() {
		String url = "https://vi.wikipedia.org/wiki/Vua_Vi%E1%BB%87t_Nam#Th%E1%BB%9Di_k%E1%BB%B3_chia_c%E1%BA%AFt";
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			Elements tables = doc.select("div.mw-parser-output table:not(.wikitable)[width!=\"1400\"]");
			Elements tables_wikitable = doc.select("div.mw-parser-output table.wikitable[width!=\"1400\"]");

			loopTables(tables);
			loopTables(tables_wikitable);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Total kings: " + kings.size());

		HelperFunctions.encodeListToJson(kings, "kings.json");
	}

	static void loopTables(Elements tables) {
		boolean isTitileRow = true;
		List<String> titleList = new ArrayList<>();

		for (Element table : tables) {
			if (!table.attr("cellpadding").isEmpty() && Integer.parseInt(table.attr("cellpadding")) == 0) {

				// loop through kings inside each table
				Elements characters = table.select("tr");
				for (Element character : characters) {

					// first get the title of each column
					if (isTitileRow) {
						isTitileRow = false;
						Elements titles = character.select("th");
						for (Element title : titles) {
							titleList.add(title.text().replaceAll("\\[.*?\\]", ""));
						}
						continue;
					}

					// then get the attributes corresponding
					Elements characterAttributes = character.select("td");

					kings.add(getKing(characterAttributes));
				}
				isTitileRow = true;
			}
		}
	}

	static King getKing(Elements characterAttributes) {
		for (int i = 0; i < kingAttributes.length; i++)
			kingAttributes[i] = "Không rõ";

		String ten = characterAttributes.get(1).text().replaceAll("\\[.*?\\]", "");
		String detailLink = "";
		String namTriVi = "";

		if (characterAttributes.size() >= 2) {

			// link to access inside
			detailLink = characterAttributes.get(1).selectFirst("a").attr("href");

			// kingYear (năm trị vì)
			if (ten.equals("An Dương Vương")) {
				namTriVi = characterAttributes.get(7).text().replaceAll("\\[.*?\\]", "");
			} else
				for (int i = characterAttributes.size() - 3; i < characterAttributes.size(); i++)
					namTriVi += characterAttributes.get(i).text().replaceAll("\\[.*?\\]", "") + " ";
			//mieu hieu
//			kingAttributes[9] = characterAttributes.get(2).text().replaceAll("\\[.*?\\]", "");
			
			//thuy hieu
			kingAttributes[10] = characterAttributes.get(3).text().replaceAll("\\[.*?\\]", "");
			
			//nien hieu
			kingAttributes[11] = characterAttributes.get(4).text().replaceAll("\\[.*?\\]", "");
			
			//ten huy
			kingAttributes[12] = characterAttributes.get(5).text().replaceAll("\\[.*?\\]", "");
			
			//the thu
			if (!characterAttributes.get(6).text().isBlank())
				kingAttributes[13] = characterAttributes.get(6).text().replaceAll("\\[.*?\\]", "");
		}

		accessDetailKing(detailLink);

		String id = HelperFunctions.normalizeString(ten).replaceAll(" ", "").toLowerCase();
		if (!kingAttributes[0].equals("Không rõ"))
			id += HelperFunctions.normalizeString(kingAttributes[0]).replaceAll(" ", "").toLowerCase();
		for (int i = 9; i < 13; i++) {
			if(!(HelperFunctions.normalizeString(kingAttributes[i]).contains("khong ro") || HelperFunctions.normalizeString(kingAttributes[i]).contains("khong co")))
				id += HelperFunctions.normalizeString(kingAttributes[i]).replaceAll(" ", "")
						.replaceAll("\\([^()]*\\)", "").toLowerCase();
		}
		
		//add other attributes to otherName
		for (int i = 9; i <= 13; i++) {
			if (i != 12 && !(kingAttributes[i].equals("Không rõ") || kingAttributes[i].equals("không có"))) {
				kingAttributes[0] += ", " + kingAttributes[i];
			}
		}
//		kingAttributes[0].replaceAll("Không rõ, ", "");
		
		System.out.println(kingAttributes[13]);

		King newKing = new King(id.replaceAll("khongro", ""), ten, // ten
				kingAttributes[0].replaceAll("Không rõ, ", ""), // ten khac
				HelperFunctions.parseYear(kingAttributes[1]), // sinh
				HelperFunctions.parseYear(kingAttributes[2]), // mat
				kingAttributes[6], // que quan
				formatKingYear(namTriVi), // tri vi
				kingAttributes[8], // mo ta
//				kingAttributes[9], // mieu hieu
				kingAttributes[10], // thuy hieu
				kingAttributes[11], // nien hieu
//				kingAttributes[12], // ten huy
				kingAttributes[13]); // the thu

		// TODO: Thêm parent, dynasty
		newKing.setDynasties(HelperFunctions.extractDynasty(kingAttributes[8], FigureScraper.refDynasties));

		// check print
//			HelperFunctions.prtKingAttributes(newKing);

		return newKing;
	}

	static String formatKingYear(String input) {
		ArrayList<Integer> numbers = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\d+"); // Regular expression to match one or more digits
		Matcher matcher = pattern.matcher(input);

		int count = 0;
		while (matcher.find() && count < 2) {
			int number = Integer.parseInt(matcher.group());
			numbers.add(number);
			count++;
		}

		if (numbers.isEmpty())
			return "Không rõ";
		if (numbers.size() >= 2)
			return numbers.get(0) + " - " + numbers.get(1);
		return numbers.get(0).toString();
	}

	static void accessDetailKing(String detailLink) {
		try {
			// for each king in the tables, access the link inside to get more data
			String url = "https://vi.wikipedia.org" + detailLink;
			Document detailDoc = Jsoup.connect(url).get();

			getDataFromTable(detailDoc);
			getDataFromParagraph(detailDoc);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static void getDataFromTable(Document detailDoc) {
		Element boxDetail = detailDoc.selectFirst("table.infobox");
		if (boxDetail == null)
			return;

		Elements trTags = boxDetail.select("tr");

		Element title, content;
		for (Element trTag : trTags) {
			title = trTag.selectFirst("th");
			content = trTag.selectFirst("td");
			if (title == null || content == null)
				continue;

			String contentString = content.text().replaceAll("\\[.*?\\]", "");
			switch (title.text()) {
			case "Sinh": {
				kingAttributes[1] = contentString;
				break;
			}
			case "Mất": {
				kingAttributes[2] = contentString;
				break;
			}
			case "Thân phụ": {
				kingAttributes[3] = contentString;
				break;
			}
			case "Thân mẫu": {
				kingAttributes[4] = contentString;
				break;
			}
			case "Triều đại": {
				kingAttributes[5] = contentString;
				break;
			}
			}
		}

	}

	static void getDataFromParagraph(Document doc) {
		Elements pTags = doc.select("p");
		StringBuilder desc = new StringBuilder();

		int limit = 1;
		if (pTags.size() > 0) {
			for (Element pTag : pTags) {
				if (limit > 3)
					break;
				desc.append(pTag.text()).append(" ");
				limit++;
			}
		}

		// get description
		kingAttributes[8] = desc.toString();

		// get dynasty
		extractDynastyName(desc.toString());
	}

	static void extractDynastyName(String text) {
		if (text.contains("vua Lê chúa Trịnh")) {
			kingAttributes[5] = "Vua Lê chúa Trịnh";
			return;
		}

		String[] keywords = { "Nhà", "nhà", "triều", "triều đại" };

		String pattern = "(?i)(" + String.join("|", keywords) + ")\\s+([A-ZÀ-Ỹ][a-zà-ỹ]+)";

		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(text);

		if (matcher.find()) {
			String dynastyName = matcher.group(2);
			if (Character.isUpperCase(dynastyName.charAt(0))) {
				kingAttributes[5] = dynastyName;
				return;
			}
		}
	}
}
