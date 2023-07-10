package backend.data.model.relic;

import java.util.ArrayList;

import backend.data.model.IObject;
import backend.data.model.figure.*;

public class Relic extends Type implements IObject {
	private String name;
	private String location;
	private String time;
	private String desc;
	private String id;
	private ArrayList<Figure> figures = new ArrayList<Figure>();

	public String getName() {
		return name.replace(name.substring(0, 1), name.substring(0, 1).toUpperCase());
	}

	public String getLocation() {
		return location;
	}

	public String getTime() {
		if (time == null) {
			return "Không rõ";
		} else if (time.equals("")) {
			return "Không rõ";
		}
		return time;
	}

	public String getDesc() {
		if (desc != null) {
			return desc;
		} else {
			return "";
		}

	}

	public String getId() {
		return id;
	}

	public ArrayList<Figure> getFigures() {
		return figures;
	}

	public void addFigure(Figure figure) {
		this.figures.add(figure);
	}

	@Override
	public String toString() {
		return "Tên di tích: " + name + "\nĐịa điểm: " + location + "\nThời gian xây dựng: " + getTime()
				+ super.toString();
	}

}