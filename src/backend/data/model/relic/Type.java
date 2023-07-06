package backend.data.model.relic;

public class Type extends Province {
    private String type;
    
    public String getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return "\nLoại di tích: " + type + super.toString();
	}
}