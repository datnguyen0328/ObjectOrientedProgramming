package backend.data.model.dynasty;

import java.util.ArrayList;
import java.util.List;

import backend.data.model.IObject;
import backend.data.model.figure.Figure;
import backend.data.service.decode.HelperFunctions;

public class Dynasty implements IObject {
	private String id;
	private String name;
	private int startYear;
	private int endYear;
	private String desc;
	private ArrayList<Figure> figures = new ArrayList<Figure>();

	public Dynasty() {
	}

	public Dynasty(String name, int startYear, int endYear, String desc) {
		this.id = HelperFunctions.normalizeString(name).toLowerCase().replaceAll(" ", "");
		this.name = name;
		this.startYear = startYear;
		this.endYear = endYear;
		this.desc = desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	public Dynasty(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name.replace(name.substring(0, 1), name.substring(0, 1).toUpperCase());
	}

	public int getStartYear() {
		return startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	public ArrayList<Figure> getFigures() {
		return figures;
	}

	public void addFigure(Figure fig) {
		figures.add(fig);
	}

	public String YearToString(int year) {
		if (year == 0) {
			return "Không rõ";
		} else if (year < 0) {
			return (-year) + " TCN";
		}

		return year + "";
	}

	@Override
	public String toString() {
		return "Tên triều đại: " + name + "\nNăm bắt đầu: " + YearToString(startYear) + "\nNăm kết thúc: "
				+ YearToString(endYear);
	}
}
