package hospital.management.system.models;

public class Doctor {
	private String doctorId;
    private String doctorName;
    private String password;
	public Doctor(String doctorId, String doctorName, String password) {
		super();
		this.doctorId = doctorId;
		this.doctorName = doctorName;
		this.password = password;
	}
	public Doctor() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
    
    
}
