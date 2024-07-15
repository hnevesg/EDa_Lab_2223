
public class Character {
private String id;
private String type;
private String subtype;
private String name;
private String gender;
private int nreferences;

public Character(String id, String type, String subtype, String name, String gender, int nreferences) {
	this.id = id;
	this.type = type;
	this.subtype = subtype;
	this.name = name;
	this.gender = gender;
	this.nreferences = nreferences;
}

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public String getSubtype() {
	return subtype;
}

public void setSubtype(String subtype) {
	this.subtype = subtype;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getGender() {
	return gender;
}

public void setGender(String gender) {
	this.gender = gender;
}

public int getNreferences() {
	return nreferences;
}

public void setNreferences(int nreferences) {
	this.nreferences = nreferences;
}


public String toString() {
	return "Character id: " + id + ", type " + type + ", subtype " + subtype + ", name " + name + ", gender" + gender
			+ ", nreferences " + nreferences;
}

}
