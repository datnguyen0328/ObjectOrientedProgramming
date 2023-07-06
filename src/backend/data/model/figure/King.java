package backend.data.model.figure;

public class King extends Figure {
	private String regnalYear;
	private String posthumousName;
	private String eraName;
	private String heirApparent;

	public King() {
	};

	public King(String id, String name, String otherName, int bornYear, int deathYear, String home, String desc,
			String regnalYear, String posthumousName, String eraName, String heirApparent) {
		super(id, name, otherName, bornYear, deathYear, home, desc);
		this.regnalYear = regnalYear;
		this.posthumousName = posthumousName;
		this.eraName = eraName;
		this.heirApparent = heirApparent;
	}

	public String getRegnalYear() {
		return regnalYear;
	}

	public void setRegnalYear(String regnalYear) {
		this.regnalYear = regnalYear;
	}

	public String getPosthumousName() {
		return posthumousName;
	}

	public void setPosthumousName(String posthumousName) {
		this.posthumousName = posthumousName;
	}

	public String getEraName() {
		return eraName;
	}

	public void setEraName(String eraName) {
		this.eraName = eraName;
	}

	public String getHeirApparent() {
		return heirApparent;
	}

	public void setHeirApparent(String heirApparent) {
		this.heirApparent = heirApparent;
	}

	@Override
	public String toString() {
		return super.toString() + "\nNăm trị vì: " + regnalYear + "\nThụy hiệu: " + posthumousName + "\nNiên hiệu: " + eraName
				+ "\nThế thứ: " + heirApparent;
	}

}
