package backend.data.model.festival;

import java.util.ArrayList;

import backend.data.model.IObject;
import backend.data.model.figure.*;

public class Festival implements IObject {
	private String id;
	private String name;
	private String place;
	private String startTime;
	private String firstTimes;
	private String[] relatedCharacter = new String[10];
	private ArrayList<Figure> relatedCharacterFigure = new ArrayList<Figure>();
	private ArrayList<String> description = new ArrayList<String>();

	public Festival() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Festival(String name, String place) {
		super();
		this.name = name;
		this.place = place;
	}

	public Festival(String name, String place, String startTime) {
		super();
		this.name = name;
		this.place = place;
		this.startTime = startTime;
	}

	public Festival(String name, String place, String startTime, String firstTimes,
			ArrayList<Figure> relatedCharacterFigure, String[] relatedCharacter, ArrayList<String> description) {
		super();
		this.name = name;
		this.place = place;
		this.startTime = startTime;
		this.firstTimes = firstTimes;
		this.relatedCharacter = relatedCharacter;
		this.description = description;
	}

	@Override
	public String getName() {
		return name.replace(name.substring(0, 1), name.substring(0, 1).toUpperCase());
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getStartTime() {
		if (startTime.equals("")) {
			return "Không rõ";
		}
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFirstTimes() {
		if (firstTimes.equals("")) {
			return "Không rõ";
		}
		return firstTimes;
	}

	public void setFirstTimes(String firstTimes) {
		this.firstTimes = firstTimes;
	}

	public ArrayList<Figure> getFigures() {
		return relatedCharacterFigure;
	}

	public void setRelatedCharacter(String[] rc) {
		this.relatedCharacter = rc;
	}

	public String[] getRelatedCharacter() {
		return relatedCharacter;
	}

	public void setRelatedCharacterFigure(ArrayList<Figure> rc) {
		this.relatedCharacterFigure = rc;
	}

	public ArrayList<String> getdescription() {
		return description;
	}

	public void addDescription(String a) {
		description.add(a);
	}

	@Override
	public String toString() {
		return "Tên: " + name + "\nĐịa điểm: " + place + "\nThời gian bắt đầu: " + getStartTime()
				+ "\nThời gian tổ chức lần đầu tiên: " + getFirstTimes();
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		String descString = "";

		for (String desc : getdescription()) {
			descString += desc + "\n";
		}

		return descString.replaceAll(
				"Trình soạn thảo sẽ tải bây giờ. Nếu một chốc nữa thông điệp này vẫn xuất hiện, xin hãy tải lại trang.",
				"");
	}

}

