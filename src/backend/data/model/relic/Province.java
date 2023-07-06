package backend.data.model.relic;

public class Province {
    private String province;

    public String getProvince() {
    	return this.province;
    }

	@Override
	public String toString() {
		return "\nTỉnh: " + province;
	}
}