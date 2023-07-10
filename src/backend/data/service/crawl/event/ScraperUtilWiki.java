package backend.data.service.crawl.event;

import backend.data.model.event.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import backend.data.service.crawl.event.*;

public class ScraperUtilWiki {
    private static final List<EventInit> eventList = new ArrayList<>();

    public static List<EventInit> standardizeEvent(String url, String baseURL) {
        EventScrapper(url, baseURL);
        // assign dynasty
        for (EventInit e : eventList) {
        	e.setDynasty(ScrapeUtilNguoiKeSu.assignDynasty(e.takeYear()));
            e.buildId();
        }
        return eventList;
    }

    private static void handleInfobox(Document detailDoc, EventInit newEvent) {
        Elements table = detailDoc.select(".vevent"); // infobox table
        Element heading = detailDoc.getElementById("firstHeading");

        // handle table.vevent cases
        Elements innerTable = table.select("tbody tr tbody");// each big row inside the table
        if (innerTable != null) {
            Elements rows = innerTable.select("tr");
            for (Element innerRow : rows) {
                if (innerRow.text().contains("Địa điểm")) {
                    Element content = innerRow.select("td").first();
                    if (content != null)
                        newEvent.setLocation(content.text());
                } else if (innerRow.text().contains("Thời gian")) {
                    continue;
                } else {
//                    System.out.println(innerRow.html());
                    Element info = innerRow.select("td").first();
                    if (info != null)
                        newEvent.addDescription(info.text());
                }
            }
//                System.out.println("after adding scanning inner table: " + newEvent.getDescription());
        }

        Elements allRows = table.select("tbody tr");

        if (heading.text().toLowerCase().contains("hiệp định")) {
            for (Element row : allRows) {
                //handle special events
                if (row.text().toLowerCase().contains("chủ nhà") || row.text().toLowerCase().contains("nơi kí")) {
                    Element location = row.select("a[href]").first();
                    if (location != null) {
                        newEvent.setLocation(location.text());
                    }
                }
            }
        }
        for (Element row : allRows) {
            // taking related figures
            if (row.text().toLowerCase().contains("lãnh đạo")) {
                Element relatedFigures = row.nextElementSibling();
                if (relatedFigures != null) {
                    Elements eachFig = relatedFigures.select("a[href]");
                    if (eachFig != null)
                        for (Element fig : eachFig) {
                            newEvent.setRelatedFigures(fig.text());
                        }
                    break;
                }
            } else if (row.text().toLowerCase().contains("tham gia")) {
                Elements sides = row.select("a[href]");
                if (sides.size() > 0) {
                    for (Element side : sides) {
                        newEvent.setRelatedFigures(side.text());
                    }
                }
                break;
            }
        }
    }

    private static void handlePlaintext(Document detailDoc, EventInit newEvent) {
        Elements des = detailDoc.select("p:has(b)");
        Element heading = detailDoc.getElementById("firstHeading");

        if (des.first() == null) {
            Element firstP = detailDoc.select("p").first();
            newEvent.addDescription(firstP.text());
            return;
        } else {
            Element firstP = detailDoc.select("p").first();
            if (firstP.text().length() > 5)
                newEvent.addDescription(firstP.text());
            else {
                Element secondP = firstP.nextElementSibling();
                if (secondP != null)
                    newEvent.addDescription(secondP.text());
            }
        }

        if (des.first().text().contains(heading.text()))
            newEvent.addDescription(des.text());
        else {
            for (Element p : des) {
                if (p.text().contains(heading.text())) {
                    newEvent.addDescription(p.text());
                    break;
                }
            }
        }
    }

    private static void additionalDescription(String detailURL, EventInit newEvent) {
        try {
            Document detailDoc = Jsoup.connect(detailURL).userAgent("Mozilla").get();
            handlePlaintext(detailDoc, newEvent);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void eventReport() {
        int nullDecription = 0;
        for (EventInit e : eventList) {
            if (e.getDescription().length() < 5) {
                nullDecription += 1;
                System.out.println(e.getName());
            }
        }
        System.out.println("number of event has null desc: " + nullDecription);
    }

    public static void showEvent() {
        for (EventInit e : eventList) {
            System.out.println(e.toString());
//            System.out.println("year: " + e.takeYear());
            System.out.println("___________________________________________");
        }
    }

    private static void EventScrapper(String url, String baseURL) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
            Elements allEvents = doc.select("p");
            int totalEvent = 0;

            int totalEventHaveInfobox = 0;

            allEvents.removeIf(e -> e.text().contains("Văn hóa"));
            allEvents.removeIf(e -> e.text().contains("tiền sử"));
            allEvents.removeIf(e -> e.text().contains("Hồng Bàng"));
            allEvents.removeIf(e -> e.text().contains("An Dương Vương"));
            allEvents.removeIf(e -> e.text().contains("Xem thêm"));

            for (Element p : allEvents) {
                StringBuilder year = new StringBuilder(p.select(":first-child").text());
//                System.out.println(year);
                totalEvent += 1;
                if (p.html().contains("href")) {
                    StringBuilder title = new StringBuilder(p.text().replace(year, ""));
                    //1945 abc
                    // thang 12 abc
                    // rarely happened
                    if (p.nextElementSibling().html().contains("<dd>")) {
                        Element dl = p.nextElementSibling();
                        if (dl != null) {
                            Element dd = dl.select("dd").first();

                            Element additionalTime = dd.select("b").first();
                            Element inBold = dd.select("b").first();
                            if (inBold != null) {
                                inBold.remove();
                            }
                            String additionalTitle = dd.text();
                            title.append("\n").append(additionalTitle);
                            if (additionalTime != null) {
                                year.append(" ").append(additionalTime.text());
                            }
                            Elements relatedLinks = dl.select("a[href]");

                            EventInit newEvent = new EventInit();
                            newEvent.setTime(year.toString());
                            newEvent.setName(title.toString());

//                            System.out.println("Rarely happened: " + title);

                            for (Element link : relatedLinks) {
                                totalEventHaveInfobox += handleDetail(baseURL + link.attr("href"), newEvent);
                            }
                            if (newEvent.getDescription().length() < 5) {
                                Element firstLink = relatedLinks.first();
                                additionalDescription(baseURL + firstLink.attr("href"), newEvent);
                            }
                            eventList.add(newEvent);
//                            System.out.println("after scanning all links: " + newEvent.toString());

                        } else {
                            System.out.println("---------------------------------------");
                        }
                    } else {
                        // 1945 abc
                        // normal cases
                        Elements relatedLinks = p.select("a[href]");

                        EventInit newEvent = new EventInit();
                        newEvent.setName(title.toString());
                        newEvent.setTime(year.toString());

//                        System.out.println("Normal case: " + p.text().replace(year, ""));
                        for (Element link : relatedLinks) {
                            totalEventHaveInfobox += handleDetail(baseURL + link.attr("href"), newEvent);
                        }
                        if (newEvent.getDescription().length() < 5) {
                            Element firstLink = relatedLinks.first();
                            additionalDescription(baseURL + firstLink.attr("href"), newEvent);
                        }
                        eventList.add(newEvent);
//                        System.out.println("after scanning all links: " + newEvent.toString());

                    }
                } else {
                    // 1945
                    // thang 3 abc
                    // thang 4 abc
                    // multiple events in one year
                    Element dl = p.nextElementSibling();
                    if (dl != null) {
                        Elements dds = dl.select("dd");
                        for (Element dd : dds) {
                            String additionalTime = " " + dd.select("b").text();
                            Element inBold = dd.getElementsByTag("b").first();
                            inBold.remove();
                            String title = dd.text();
                            year.append(additionalTime);
                            Elements relatedLinks = dd.select("a[href]");
//                            System.out.println("in one year: " + " " + title);

                            EventInit newEvent = new EventInit();
                            newEvent.setTime(year.toString());
                            newEvent.setName(title);
                            for (Element link : relatedLinks) {
                                totalEventHaveInfobox += handleDetail(baseURL + link.attr("href"), newEvent);
                            }
                            if (newEvent.getDescription().length() < 5) {
                                Element firstLink = relatedLinks.first();
                                additionalDescription(baseURL + firstLink.attr("href"), newEvent);
                            }
                            eventList.add(newEvent);
//                            System.out.println("after scanning all links: " + newEvent.toString());
                        }
                    }
                }
            }
//            System.out.println(totalEvent + " and total event has infobox " + totalEventHaveInfobox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int handleDetail(String detailURL, EventInit newEvent) {
        try {
            Document detailDoc = Jsoup.connect(detailURL).userAgent("Mozilla").get();
            Element heading = detailDoc.getElementById("firstHeading");
            Element infoBoxTable = detailDoc.select(".infobox").first();

            newEvent.setId("wiki");

            if (infoBoxTable != null) {
                // handle infobox cases
                if (infoBoxTable.select(".geography").first() != null) {
                    newEvent.setLocation(heading.text());
                }
                if (detailDoc.select(".vcard").first() != null) {
                    // add description
                    Elements des = detailDoc.select("p:has(b)");

                    if (des.first().text().contains(heading.text()))
                        newEvent.addDescription(des.text());
                    else {
                        for (Element p : des) {
                            if (p.text().contains(heading.text())) {
                                newEvent.addDescription(p.text());
                                break;
                            }
                        }
                    }
                }
                if (detailDoc.select(".biography").first() != null) {
                    newEvent.setRelatedFigures(heading.text());
                }
                if (detailDoc.select(".infobox").first() != null) {
                    // adding related figures
                    Element infobox = detailDoc.select(".infobox").first();
                    Elements infos = infobox.select("tr");
                    for (Element info : infos) {
                        if (info.text().toLowerCase().contains("sinh")) {
                            newEvent.setRelatedFigures(heading.text());
                            break;
                        }
                    }
                    // problem here u need to pass the newEvent from to handleDetail to avoid duplicate newEvent
                }
                if (detailDoc.select(".vevent").first() != null) {
                    handleInfobox(detailDoc, newEvent);
                }
                return 1;
            } else {
                // handle plain text cases
                handlePlaintext(detailDoc, newEvent);
                return 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
