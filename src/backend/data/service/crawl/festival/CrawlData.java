package backend.data.service.crawl.festival;

import java.io.IOException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

import backend.data.model.figure.*;
import backend.data.service.decode.Utils;
import backend.data.constant.Constant;
import backend.data.model.festival.*;

public class CrawlData {
	public static void main(String[] args) {
		ArrayList<Festival> festivals = new ArrayList<Festival>();
		Gson gson = new Gson();

		ArrayList<Figure> figures = new ArrayList<Figure>();
		figures = Utils.figureDecode(Constant.FIGURE_FILE_NAME);

		try {
			String url = "https://vi.wikipedia.org/wiki/L%E1%BB%85_h%E1%BB%99i_Vi%E1%BB%87t_Nam#:~:text=M%E1%BB%99t%20s%E1%BB%91%20l%E1%BB%85%20h%E1%BB%99i%20l%E1%BB%9Bn,ph%E1%BB%91%20%C4%90%C3%A0%20N%E1%BA%B5ng)...";
			Document doc = Jsoup.connect(url).get();
			Elements data = doc.select("table.prettytable tr:not(:first-child)");
			boolean r = true;
			for (Element d : data) {
				Elements e = d.select("td");
				Festival f = new Festival();
				f.setStartTime(e.get(0).text());
				f.setPlace(e.get(1).text());
				f.setName(e.get(2).text());
				f.setFirstTimes(e.get(3).text());
				String[] rc = e.get(4).text().toString().split(",", 10);
				f.setRelatedCharacter(rc);
				if (rc.length == 0) {
					f.setRelatedCharacterFigure(new ArrayList<Figure>());
				} else {
					ArrayList<Figure> figuresList = new ArrayList<Figure>();
					for (String a : rc) {
						for (Figure fi : figures) {
							if (Utils.normalizeString(a).equals(Utils.normalizeString(fi.getName()))) {
								figuresList.add(fi);
							}
						}
					}
					f.setRelatedCharacterFigure(figuresList);
				}
				// f.setRelatedCharacter(rc);
				f.setId(Utils.normalizeString(e.get(2).text()));

				Elements hrefElement = e.get(2).select("a");
				String hrefDetail = hrefElement.get(0).absUrl("href");

				f.addDescription(getDetail(hrefDetail));
				festivals.add(f);
			}

			String json = gson.toJson(festivals);
			r = Utils.SaveStringFile(json, Constant.FESTIVAL_FILE_NAME);

			if (r)
				System.out.println("Save Successfully");
			else {
				System.out.println("Save fail");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getDetail(String url) {
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("\\[(\\d{1,2}|100)\\]");
		try {
			Document doc = Jsoup.connect(url).get();
			Elements data = doc.select("p");
			// if(data == null) return"";
			for (Element element : data) {
				if (element.text().contains("·") || element.text().contains("•"))
					break;
				Matcher matcher = pattern.matcher(element.text().toString());

				StringBuffer modifiedSentence = new StringBuffer();
				boolean matchFound = false;

				while (matcher.find()) {
					matcher.appendReplacement(modifiedSentence, "");
					matchFound = true;
				}

				if (matchFound) {
					matcher.appendTail(modifiedSentence);
					sb.append(modifiedSentence);
					sb.append(" \n");
				} else {
					sb.append(element.text().toString());
					sb.append(" \n");
				}
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
