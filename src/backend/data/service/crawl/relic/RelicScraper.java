package backend.data.service.crawl.relic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import backend.data.model.relic.*;

public class RelicScraper {
    static Document document;
    static ArrayList<Relic> locals = new ArrayList<Relic>();

    static String normalizeString(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "").replace("Đ", "D").replace("đ", "d")
                .replace(" – ", "-").replace("- ", "-").replace(" -", "-").replace("–", "-").replace("—", "-");
    }

    static String removeBrackets(String s) {
        return s.replaceAll("\\[.?\\]", "");
    }

    public static void getHTML(String url) {
        try {
            document = Jsoup.connect(url).get();

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public static void main(String[] args) throws Exception {
        /*
         * List<String> Figures = new ArrayList<String>();
         * Figures = KingList.Figures;
         */
        // List<Figure> Figures = new ArrayList<Figure>();
        // Figures = EncodeDecode.decodedFigureList(2);
        getHTML("https://vi.m.wikipedia.org/wiki/Danh_s%C3%A1ch_Di_t%C3%ADch_qu%E1%BB%91c_gia_Vi%E1%BB%87t_Nam");
        Elements tables = document.select("div.mw-parser-output table.wikitable.sortable");
        Elements provinces = document.select("div.mw-parser-output section h3 span.mw-headline");
        Element pro = new Element("div.mw-parser-output section h3 span.mw-headline");
        pro.html("Thu do Ha Noi");
        provinces.add(24, pro);
        File file1 = new File("E:/data2.txt");
        File file2 = new File("E:/data3.json");
        FileWriter writer2 = new FileWriter(file1);
        FileWriter writer3 = new FileWriter(file2);
        int j = 0;
        int k = 0;
        for (Element table : tables) {
            Elements historicals = table.select("tr");
            for (Element his : historicals) {
            	if(!his.html().contains("th"))
            	{
	            	Relic local = new Relic();
	            	local.setProvince(provinces.get(k).text());
	                writer2.write("\n");
	                Elements infos = his.select("td");
	                int i = 0;
	                if(local.getProvince().contains("Ninh Bình")) {
	                	for (Element info : infos) {
		                    switch (i) {
		                        case 1:
		                            writer2.write("Di tich: ");
		                            local.setName(info.text());
		
		                            
		                            local.setId(normalizeString(info.text()).toLowerCase().replace(" ", ""));
		                            break;
		                        case 2:
		                            writer2.write("Vi tri: ");
		                            local.setLocation(info.text());
		                            break;
		                        case 3:
		                            writer2.write("Loai di tich: ");
		                            local.setType(info.text());
		                            break;
		                       
		                    }
		                    writer2.write(normalizeString(info.text().replaceAll("\\[.?\\]", "")));
		                    writer2.write("\n");
		                    i++;
		                }
	                	continue;
	                }
	                for (Element info : infos) {
	                    switch (i) {
	                        case 0:
	                            writer2.write("Di tich: ");
	                            local.setName(info.text());
	
	                            Elements links = info.select("a[href]");
	                            for (Element link : links) {
	                                if (!link.attr("abs:href").isEmpty()) {
	                                    String desString = "";
	                                    Document doc = Jsoup.connect(link.attr("abs:href")).get();
	                                    Elements description = doc.select("div.mw-parser-output section.mf-section-0");
	                                    if (!description.isEmpty()) {
	                                        for (Element des : description) {
	                                            desString += des.text() + " ";
	                                        }
	                                        local.setDesc(desString);
	                                    } else {
	                                        local.setDesc("");
	                                    }
	                                    /*
	                                     * if (!desString.isEmpty()) {
	                                     * for (int count = 0; count < 10; count++) {
	                                     * if (desString.contains(normalizeString(Figures.get(count).getName()))) {
	                                     * local.setFigure(Figures.get(count));
	                                     * }
	                                     * }
	                                     * }
	                                     */
	                                }
	                            }
	
	                            local.setId(normalizeString(info.text()).toLowerCase().replace(" ", ""));
	                            break;
	                        case 1:
	                            writer2.write("Vi tri: ");
	                            local.setLocation(info.text());
	                            break;
	                        case 2:
	                            writer2.write("Loai di tich: ");
	                            local.setType(info.text());
	                            break;
	                        case 3:
	                            writer2.write("Thoi gian: ");
	                            local.setTime(info.text());
	                            break;
	                    }
	                    
	                    writer2.write(normalizeString(info.text().replaceAll("\\[.?\\]", "")));
	                    writer2.write("\n");
	                    i++;
	                }
	                writer2.write(normalizeString(provinces.get(k).text()));
	                writer2.write("\n");
	                locals.add(local);
	                j++;
            	}
            }
            k++;

        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(locals);
        writer3.write(json);
        System.out.println(j);
        System.out.println(k);
        writer2.close();
        writer3.close();
    }
}