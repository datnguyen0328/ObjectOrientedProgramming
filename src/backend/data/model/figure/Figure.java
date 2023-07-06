package backend.data.model.figure;

import java.util.ArrayList;

import backend.data.model.*;

public class Figure implements IObject {
	protected String id;
	protected String name;
	protected String otherName;
	protected int bornYear;
	protected int deathYear;
	protected ArrayList<String> parents = new ArrayList<>();
	protected ArrayList<String> dynasties = new ArrayList<>();
	protected String home;
	protected String desc;

	public Figure() {
	}

	public Figure(String id, String name, String otherName, int bornYear, int deathYear, ArrayList<String> parents,
			ArrayList<String> dynasties, String home, String desc) {
		this.id = id; // 0 OK
		this.name = name; // 1 OK
		this.otherName = otherName; // 2 OK
		this.bornYear = bornYear; // 3 OK
		this.deathYear = deathYear; // 4 OK
		this.parents = parents; // ? OK
		this.dynasties = dynasties; // ? hmmm...
		this.home = home; // 5 OK
		this.desc = desc; // 6 OK
	}

	public Figure(String id, String name, String otherName, int bornYear, int deathYear, ArrayList<String> dynasties,
			String home, String desc) {
		this.id = id; // 0 OK
		this.name = name; // 1 OK
		this.otherName = otherName; // 2 OK
		this.bornYear = bornYear; // 3 OK
		this.deathYear = deathYear; // 4 OK
//		this.parents = parents; 		// ? OK
		this.dynasties = dynasties; // ? hmmm...
		this.home = home; // 5 OK
		this.desc = desc; // 6 OK
	}

	public Figure(String id, String name, String otherName, int bornYear, int deathYear, String home, String desc) {
		this.id = id; // 0 OK
		this.name = name; // 1 OK
		this.otherName = otherName; // 2 OK
		this.bornYear = bornYear; // 3 OK
		this.deathYear = deathYear; // 4 OK
//		this.parents = parents; 		// ? OK
//		this.dynasties = dynasties; 	// ? hmmm...
		this.home = home; // 5 OK
		this.desc = desc; // 5 OK
	}
	
	public ArrayList<Figure> getFigures() {
		return null;
	}

	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	public String getOtherName() {
		return otherName;
	}

	public int getBornYear() {
		return bornYear;
	}

	public int getDeathYear() {
		return deathYear;
	}

	public ArrayList<String> getDynasties() {
		return dynasties;
	}

	public ArrayList<String> getParents() {
		return parents;
	}

	public String getHome() {
		return home;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public void setBornYear(int bornYear) {
		this.bornYear = bornYear;
	}

	public void setDeathYear(int deathYear) {
		this.deathYear = deathYear;
	}

	public void setParents(ArrayList<String> parents) {
		this.parents = parents;
	}

	public void setDynasties(ArrayList<String> dynasties) {
		this.dynasties = dynasties;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String parentsToString() {
		String parentsString = "";

		for (int i = 0; i < parents.size(); i++) {
			if (i == parents.size() - 1) {
				parentsString += parents.get(i);
			} else {
				parentsString += parents.get(i) + ",";
			}
		}

		if (parentsString.equals("")) {
			return "Không rõ";
		}

		return parentsString;
	}

	public String dynastiesToString() {
		String dynastiesString = "";

		for (int i = 0; i < dynasties.size(); i++) {
			if (i == dynasties.size() - 1) {
				dynastiesString += dynasties.get(i);
			} else {
				dynastiesString += dynasties.get(i) + ", ";
			}
		}

		if (dynastiesString.equals("")) {
			return "Không rõ";
		}

		return dynastiesString;
	}
	
	public String YearToString(int year) {
		if(year == 0) {
			return "Không rõ";
		}else if(year < 0) {
			return (-year) + "TCN";
		}
		
		return year +"";
	}

	@Override
	public String toString() {
		return "Tên: " + name + "\nTên khác: " + otherName + "\nNăm sinh: " + YearToString(bornYear) + "\nNăm mất: " + YearToString(deathYear)
				+ "\nCha mẹ: " + parentsToString() + "\nTriều đại: " + dynastiesToString() + "\nQuê quán: " + home;
	}

}
