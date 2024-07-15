

public class Establishment {
	private String type;
	private String name;
	private int postal_code;
	private String town;
	private String province;
	private int total_visits;

	public Establishment(String type, String name, int postal_code, String town, String province, int total_visits) {
		this.type = type;
		this.name = name;
		this.postal_code = postal_code;
		this.town = town;
		this.province = province;
		this.total_visits = total_visits;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(int postal_code) {
		this.postal_code = postal_code;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public int getTotal_visits() {
		return total_visits;
	}

	public void setTotal_visits(int total_visits) {
		this.total_visits = total_visits;
	}

	public String toString() {
		return "Establishment type: " + type + ", name: " + name + ", postal_code: " + postal_code + ", town: " + town
				+ ", province: " + province + ", and total_visits: " + total_visits + "\n";
	}
}