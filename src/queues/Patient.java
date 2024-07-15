
public class Patient implements Comparable <Patient> {
private String DNI;
private String specialty;
private String severity;
private double time;

public Patient(String dNI, String specialty, String severity, double time) {
	
	DNI = dNI;
	this.specialty = specialty;
	this.severity = severity;
	this.time=time;
}

public String getDNI() {
	return DNI;
}

public void setDNI(String dNI) {
	DNI = dNI;
}

public String getSpecialty() {
	return specialty;
}

public void setSpecialty(String specialty) {
	this.specialty = specialty;
}

public String getSeverity() {
	return severity;
}

public void setSeverity(String severity) {
	this.severity = severity;
}

public double getTime() {
	return time;
}


public void setTime(double time) {
	this.time = time;
}


public String toString() {
	return "Patient: DNI " + DNI + ", specialty " + specialty + ", severity " + severity;
}

public int compareTo(Patient o) {
	return 0;
}

	
}
