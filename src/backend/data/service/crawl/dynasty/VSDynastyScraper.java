package backend.data.service.crawl.dynasty;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import backend.data.model.dynasty.*;
import backend.data.service.decode.HelperFunctions;

public class VSDynastyScraper {
	static private ArrayList<Dynasty> dynasties = new ArrayList<Dynasty>();
	static private String dynastyAttributes[] = new String[2];

	public static void crawl() {
		String url = "https://vansu.vn/viet-nam/nien-bieu-lich-su";
		try {
			Document doc = Jsoup.connect(url).get();

			// outer div
			Element parentDiv = doc.select("div.ui").get(3);

			int RedundantNodeCounter = 1;

			for (Node child : parentDiv.childNodes()) {

				// several first nodes are redundant
				if (RedundantNodeCounter < 12) {
					RedundantNodeCounter++;
					continue;
				}

				// skip if the node is <br>, <p>
				String nodeName = child.nodeName();
				if (nodeName.equals("br") || nodeName.equals("p"))
					continue;

				if (nodeName.equals("div")) {
					// reset attributes
					for (int i = 0; i < 2; i++)
						dynastyAttributes[i] = "Không rõ";

					// get name & years
					String name = ((Element) child).text();
					extractYears(name);

					String detail = "";
					Node nextNode = child.nextSibling();
					while (nextNode != null && !nextNode.nodeName().equals("div")) {
						if (nextNode instanceof Element) {
							detail += ((Element) nextNode).text() + " ";
						} else {
							// remove the figure listing parts
							if (nextNode.toString().contains("1. ") || nextNode.toString().contains("5. "))
								break;

							detail += nextNode.toString() + " ";
						}

						nextNode = nextNode.nextSibling();
					}

					if (detail.isEmpty())
						detail = "Không rõ";
					
					int start = HelperFunctions.parseYear(dynastyAttributes[0]);
					int end = HelperFunctions.parseYear(Integer.parseInt(dynastyAttributes[1]) == 0 ? "2023": dynastyAttributes[1]);

					if(start > end) start *= -1;
					if(end == 258 || end == 208) end *= -1;
					
					// after crawling data, get add to list
					Dynasty tmpDynasty = new Dynasty(
							name.replaceAll("\\([^()]*\\)", ""),
							start,
							end,
							detail);
					dynasties.add(tmpDynasty);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		HelperFunctions.encodeListToJson(dynasties, "vs_dynasies.json");
	}

	static void extractYears(String text) {
		int startYear = 0;
		int endYear = 0;

		Pattern pattern = Pattern.compile("(\\d+)\\s*(trCN)?");
		Matcher matcher = pattern.matcher(text);

		if (matcher.find()) {
			String startYearStr = matcher.group(1);
			String startYearModifier = matcher.group(2);

			startYear = Integer.parseInt(startYearStr);

			if (startYearModifier != null && startYearModifier.equals("trCN")) {
				startYear = -startYear;
			}

			if (matcher.find()) {
				String endYearStr = matcher.group(1);
				String endYearModifier = matcher.group(2);

				endYear = Integer.parseInt(endYearStr);

				if (endYearModifier != null && endYearModifier.equals("trCN")) {
					endYear = -endYear;
				}
			}
		}

		if (startYear > 0 && endYear < 0)
			startYear = -startYear;

		dynastyAttributes[0] = startYear + "";
		dynastyAttributes[1] = endYear + "";
	}
}