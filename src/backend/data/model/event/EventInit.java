package backend.data.model.event;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import backend.data.model.IObject;
import backend.data.model.figure.Figure;

public class EventInit implements IObject{
    private String id;
    private String name;
    private String time;
    private String location;
    private Set<String> relatedFigures;
    private String dynasty;
    private StringBuilder description;

    public EventInit() {
        this.description = new StringBuilder();
        this.relatedFigures = new HashSet<>();
    }

    public EventInit(String name, String time, String location) {
        this.name = name;
        this.time = time;
        this.location = location;
    }

    public EventInit(String name, String time, String location, String dynasty, String description) {
        this.name = name;
        this.time = time;
        this.location = location;
        this.dynasty = dynasty;
        this.description = new StringBuilder(description);
    }

    public void buildId() {
        String input = this.getName().toLowerCase().replaceAll(" ", "");
        String temp = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        temp = pattern.matcher(temp).replaceAll("");
        this.setId(temp.replaceAll("đ", "d"));
    }

    // finised for nguoi ke su
    public int takeYear() {
        if (this.getId().equals("wiki")) {
            int year = 0;
            String[] words = this.getTime().split(" ");

            if (this.getTime().contains("TCN")) {
                if (this.getTime().contains("VII")) {
                    year = -700;
                    return year;
                } else {
                    year = Integer.parseInt(words[0]) * -1;
                    return year;
                }
            }

            if (words[0].contains("-")) {
                String[] temp = words[0].split("-");
                year = Integer.parseInt(temp[0]);
                return year;
            } else if (words[0].contains("–")) {
                String[] temp = words[0].split("–");
                year = Integer.parseInt(temp[0]);
                return year;
            } else {
                year = Integer.parseInt(words[0]);
                return year;
            }
        } else {
            // take year in case of nguoi ke su
            String eventTitle = this.getName();
            String time = this.getTime();
            String[] words = eventTitle.split(" "); // regular format
            int year = 0;
            for (String word : words) {
                if (time.contains(word)) {
                    try {
                        if (Integer.parseInt(word) > 31) {
                            year = Integer.parseInt(word);
                            break;
                        } else {
                            continue;
                        }
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
            }
            if (year != 0) {
//                System.out.println(year);
            } else {
                if (eventTitle.contains("(")) {
                    String[] splitInTime = time.split(" ");
                    try {
                        year = Integer.parseInt(splitInTime[splitInTime.length - 1].substring(0, 4));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                } else {
                    String[] wordsInTime = time.split(" ");
                    if (wordsInTime.length > 1) {
                        try {
                            if (wordsInTime[wordsInTime.length - 1].contains("-")) {
                                String[] temp = wordsInTime[wordsInTime.length - 1].split("-");
                                year = Integer.parseInt(temp[1]);
                            } else
                                year = Integer.parseInt(wordsInTime[wordsInTime.length - 1]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String[] splitByHyphen = time.split("-");
                        try {
                            year = Integer.parseInt(splitByHyphen[splitByHyphen.length - 1]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return year;
        }
    }

    public void addDescription(String description) {
        this.description.append(description);
    }

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<String> getRelatedFigure() {
        return relatedFigures;
    }

    public void setRelatedFigure(Set<String> relatedFigure) {
        this.relatedFigures = relatedFigure;
    }

    public void setRelatedFigures(String fig) {
        this.relatedFigures.add(fig);
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }

    public String getName() {
    	if(name.substring(0,1).equals(" ")) {
    		return name.substring(1).replace(name.substring(0, 1), name.substring(0, 1).toUpperCase());
    	}
        return name.replace(name.substring(0, 1), name.substring(0, 1).toUpperCase());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public StringBuilder getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = new StringBuilder(description);
    }

    @Override
	public String toString() {
		return "Tên sự kiện: " + name + "\nThời gian xảy ra: " + time + "\nĐịa điểm: " + location;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return description.toString();
	}

	@Override
	public ArrayList<Figure> getFigures() {
		// TODO Auto-generated method stub
		return null;
	}
}
