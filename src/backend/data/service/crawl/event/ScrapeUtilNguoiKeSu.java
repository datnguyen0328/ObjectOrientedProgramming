package backend.data.service.crawl.event;

import backend.data.model.event.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class ScrapeUtilNguoiKeSu {
    private static final List<EventInit> eventList = new ArrayList<EventInit>();
    private static final List<String> keyWords = new ArrayList<>();

    private static Map<ArrayList<Integer>, String> RefDynasty = new HashMap<>(30);

    static {
        keyWords.add("Thời gian");
        keyWords.add("Địa điểm");
        keyWords.add("Kết quả");

        RefDynasty.put(new ArrayList<Integer>() {{
            add((-2879));
            add(-258);
        }}, "Hồng Bàng \u0026 Văn Lang");
        RefDynasty.put(new ArrayList<Integer>() {{
            add((-258));
            add(-217);
        }}, "Âu Lạc \u0026 Nam Việt");
        RefDynasty.put(new ArrayList<Integer>() {{
            add((1460));
            add(1497);
        }}, "nhà Lê");
        RefDynasty.put(new ArrayList<Integer>() {{
            add((1527));
            add(1529);
        }}, "nhà Mạc");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(-218);
            add(39);
        }}, "Bắc thuộc lần I");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(40);
            add(43);
        }}, "Trưng Nữ Vương");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(34);
            add(543);
        }}, "Bắc thuộc lần II");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(544);
            add(602);
        }}, "Nhà Lý \u0026 Nhà Triệu");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(602);
            add(905);
        }}, "Bắc thuộc lần III");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(905);
            add(938);
        }}, "Thời kỳ xây nền tự chủ");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(939);
            add(965);
        }}, "Nhà Ngô");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(968);
            add(980);
        }}, "Nhà Đinh");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(980);
            add(1009);
        }}, "Nhà Tiền Lê");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1010);
            add(1225);
        }}, "Nhà Lý");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1225);
            add(1400);
        }}, "Nhà Trần");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1400);
            add(1407);
        }}, "Nhà Hồ");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1407);
            add(1413);
        }}, "Nhà Hậu Trần");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1407);
            add(1427);
        }}, "Bắc thuộc lần IV");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1427);
            add(1789);
        }}, "Nhà Hậu Lê");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1533);
            add(1592);
        }}, "Nam Bắc Triều");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1627);
            add(1777);
        }}, "Trịnh Nguyễn Phân Tranh");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1778);
            add(1802);
        }}, "Nhà Tây Sơn");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1802);
            add(1945);
        }}, "Nhà Nguyễn");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1884);
            add(1945);
        }}, "Pháp Thuộc");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1945);
            add(1975);
        }}, "Việt Nam Dân Chủ Cộng Hoà");
        RefDynasty.put(new ArrayList<Integer>() {{
            add(1976);
            add(10000);
        }}, "Cộng hoà xã hội chủ nghĩa Việt Nam");

    }

    public static String assignDynasty(int year) {
        for (Map.Entry<ArrayList<Integer>, String> e : RefDynasty.entrySet()) {
            if (year >= e.getKey().get(0) && year <= e.getKey().get(1)) {
                return e.getValue();
            }
        }
        return "uncategorized";
    }

    public static List<EventInit> standardizedEvents(String url, String baseURL) {
        List<EventInit> eventNguoiKeSu = EventScrapper(url, baseURL);

        // set time and desc for null fields
        for (EventInit e : eventNguoiKeSu) {
            if (e.getTime() == null) {
                String[] words = e.getName().split(" ");
                e.setTime(words[words.length - 1]);
            }
        }
        // assign dynasty
        for (EventInit e : eventNguoiKeSu) {
            e.setDynasty(assignDynasty(e.takeYear()));
            e.buildId();
        }
        return eventNguoiKeSu;
    }

    public static List<EventInit> EventScrapper(String url, String baseURL) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
            Elements links = doc.select("p.readmore a[href]");

            Element isTheEnd = doc.select(".active.page-item").first().nextElementSibling();

            String nextLink = baseURL + isTheEnd.select(".page-link").attr("href");

            for (Element link : links) {
                handleDetail(baseURL + link.attr("href"));
            }
            if (nextLink.equals(baseURL)) {
                return eventList;
            }
            EventScrapper(nextLink, baseURL);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    public static void handleDetail(String detailURL) {
        try {
            EventInit newEvent = new EventInit();
            newEvent.setId("nguoikesu");
            Document detailDoc = Jsoup.connect(detailURL).userAgent("Mozilla").get();
            Elements table = detailDoc.select("table[cellpadding=0][cellspacing=0][width=100%]");
            Elements header = detailDoc.select("h1");
            Elements relatedFigures = detailDoc.select(".table-striped a[href]");

            Set<String> figures = new HashSet<String>();
            for (Element e : relatedFigures) {
                if (e.attr("href").contains("nhan-vat")) {
                    figures.add(e.text());
                }
            }

            newEvent.setName(header.text());
            Elements rows = table.select("tbody tr");
            for (Element row : rows) {
                Elements cells = row.select("td");
                if (cells.size() > 1) {
                    String label = cells.get(0).text();
                    String value1 = cells.get(1).text();
                    if (label.compareTo(keyWords.get(0)) == 0) {
                        newEvent.setTime(value1);
                    } else if (label.compareTo(keyWords.get(1)) == 0) {
                        newEvent.setLocation(value1);
                    } else if (label.compareTo(keyWords.get(2)) == 0) {
                        newEvent.setDescription(label + ": " + value1);
                    } else {
                        newEvent.addDescription(label + ": " + value1);
                    }
                }
            }
            newEvent.setRelatedFigure(figures);
            eventList.add(newEvent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
