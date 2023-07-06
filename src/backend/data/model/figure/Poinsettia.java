package backend.data.model.figure;

import java.util.ArrayList;

public class Poinsettia extends Figure {
	String king;
	String graduatedYear;

	public Poinsettia() {
	}

	public Poinsettia(String id, String name, String otherName, int bornYear, int deathYear, ArrayList<String> parents,
			ArrayList<String> dynasties, String home, String king, String desc, String graduatedYear) {
		super(id, name, otherName, bornYear, deathYear, parents, dynasties, home, desc);
		this.king = king;

		this.graduatedYear = graduatedYear;
	}

	public Poinsettia(String id, String name, String otherName, int bornYear, int deathYear, String home,
			String kingYear, String desc, String graduatedYear) {
		super(id, name, otherName, bornYear, deathYear, home, desc);
		this.king = kingYear;
		this.graduatedYear = graduatedYear;
	}

	public String getKingYear() {
		return king;
	}

	public String getGraduatedYear() {
		return graduatedYear;
	}

	public void setKingYear(String kingYear) {
		this.king = kingYear;
	}

	public void setGraduatedYear(String gradYear) {
		this.graduatedYear = gradYear;
	}

	@Override
	public String toString() {
		return super.toString() + "\nThời vua: " + king + "\nNăm đỗ trạng nguyên: " + graduatedYear;
	}
}
