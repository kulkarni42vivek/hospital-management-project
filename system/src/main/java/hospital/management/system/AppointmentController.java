package hospital.management.system;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.management.system.models.Appointment;
import hospital.management.system.models.Doctor;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "*")
public class AppointmentController {

	private static final String URL = "jdbc:mysql://localhost:3306/hospital";
	private static final String USER = "root";
	private static final String PASSWORD = "1234";

	@PostMapping("/create")
	public ResponseEntity<String> createDoctor(@RequestBody Doctor doctor) {
		String doctorInsertSQL = "INSERT INTO doctor (doctorId, doctorName) VALUES (?, ?)";
		String userInsertSQL = "INSERT INTO users (userId, password, role, firstName, lastName) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

			PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE userId = ?");
			checkStmt.setString(1, doctor.getDoctorId());
			ResultSet rs = checkStmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				return ResponseEntity.ok("Doctor already exists.");
			}

			// Insert into doctor table
			PreparedStatement doctorStmt = conn.prepareStatement(doctorInsertSQL);
			doctorStmt.setString(1, doctor.getDoctorId());
			doctorStmt.setString(2, doctor.getDoctorName());
			doctorStmt.executeUpdate();

			// Insert into users table with role DOCTOR
			PreparedStatement userStmt = conn.prepareStatement(userInsertSQL);
			userStmt.setString(1, doctor.getDoctorId());
			userStmt.setString(2, doctor.getPassword());
			userStmt.setString(3, "DOCTOR");
			userStmt.setString(4, doctor.getDoctorId());
			userStmt.setString(5, "");

			userStmt.executeUpdate();

			return ResponseEntity.ok("Doctor created successfully!");

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error while creating doctor: " + e.getMessage());
		}
	}

	@GetMapping("/getdoctordata")
	public ResponseEntity<List<Doctor>> getDoctorData() {
		List<Doctor> doctors = new ArrayList<>();

		String query = "SELECT doctorId, doctorName FROM doctor";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(query);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				Doctor doctor = new Doctor();
				doctor.setDoctorId(rs.getString("doctorId"));
				doctor.setDoctorName(rs.getString("doctorName"));
				doctors.add(doctor);
			}

			return ResponseEntity.ok(doctors);

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(doctors); // return empty list on failure
		}
	}

	@PostMapping("/bookAppointment")
	public ResponseEntity<?> bookAppointment(@RequestBody Appointment appointment) {

		String insertSQL = "INSERT INTO appointments (patient_id, doctor_id, appointment_date, prescription) VALUES (?, ?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
				PreparedStatement stmt = conn.prepareStatement(insertSQL)) {

			stmt.setString(1, appointment.getPatientId());
			stmt.setString(2, appointment.getDoctorId());
			stmt.setDate(3, Date.valueOf(appointment.getAppointmentDate()));
			stmt.setString(4, appointment.getPrescription());

			int rows = stmt.executeUpdate();

			if (rows > 0) {
				return ResponseEntity.ok(Map.of("message", "Appointment booked successfully!"));
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(Map.of("message", "Failed to book appointment."));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("message", "Error: " + e.getMessage()));
		}
	}
	@GetMapping("/getappointmentdata")
	public ResponseEntity<List<Map<String, Object>>> getAppointmentData() {
	    List<Map<String, Object>> appointments = new ArrayList<>();

	    String sql = "SELECT * FROM appointments";

	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
	         PreparedStatement stmt = conn.prepareStatement(sql);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Map<String, Object> row = new HashMap<>();
	            row.put("appointmentId", rs.getInt("appointment_id"));
	            row.put("patientId", rs.getString("patient_id"));
	            row.put("doctorId", rs.getString("doctor_id"));
	            row.put("appointmentDate", rs.getDate("appointment_date"));
	            row.put("prescription", rs.getString("prescription"));
	            appointments.add(row);
	        }

	        return ResponseEntity.ok(appointments);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

}
