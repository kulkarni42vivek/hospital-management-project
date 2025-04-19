package hospital.management.system.models;

public class Appointment {
	private String patientId;
    private String doctorId;
    private String appointmentDate;
    private String prescription;
	public Appointment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Appointment(String patientId, String doctorId, String appointmentDate, String prescription) {
		super();
		this.patientId = patientId;
		this.doctorId = doctorId;
		this.appointmentDate = appointmentDate;
		this.prescription = prescription;
	}
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
	public String getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getPrescription() {
		return prescription;
	}
	public void setPrescription(String prescription) {
		this.prescription = prescription;
	}
    
    
}
