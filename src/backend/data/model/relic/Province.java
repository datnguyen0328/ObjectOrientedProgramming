package backend.data.model.relic;

public class Province {
    private String province;

    public void setProvince(String province) {
		this.province = province;
	}

	public String getProvince() {
    	return this.province;
    }

	@Override
	public String toString() {
		return "\nTỉnh: " + province;
	}
}