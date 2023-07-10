package backend.data.model.relic;

public class Type extends Province {
    private String type;
    
    public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "\nLoại di tích: " + type + super.toString();
	}
}