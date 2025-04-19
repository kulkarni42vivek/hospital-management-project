package hospital.management.system;

import java.sql.Connection;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/appoint")
@CrossOrigin(origins = "*")
public class DoctorController {

	
	
	private static final String URL = "jdbc:mysql://localhost:3306/hospital";
	private static final String USER = "root";
	private static final String PASSWORD = "1234";
	
	@GetMapping("/appointmentsbydoctor")
	public ResponseEntity<List<Map<String, Object>>> getAppointmentsByDoctor(@RequestParam String doctorId) {
	    List<Map<String, Object>> appointments = new ArrayList<>();

	    String sql = "SELECT * FROM appointments WHERE doctor_id = ?";

	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setString(1, doctorId);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {
	            Map<String, Object> appt = new HashMap<>();
	            appt.put("appointmentId", rs.getInt("appointment_id"));
	            appt.put("patientId", rs.getString("patient_id"));
	            appt.put("appointmentDate", rs.getDate("appointment_date"));
	            appt.put("prescription", rs.getString("prescription"));
	            appointments.add(appt);
	        }

	        return ResponseEntity.ok(appointments);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	
	@PutMapping("/updatePrescription")
	public ResponseEntity<Map<String, String>> updatePrescription(@RequestBody Map<String, String> payload) {
	    String appointmentId = payload.get("appointmentId");
	    String prescription = payload.get("prescription");

	    String sql = "UPDATE appointments SET prescription = ? WHERE appointment_id = ?";
	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	        PreparedStatement stmt = conn.prepareStatement(sql);
	        stmt.setString(1, prescription);
	        stmt.setInt(2, Integer.parseInt(appointmentId));

	        int rowsUpdated = stmt.executeUpdate();
	        Map<String, String> res = new HashMap<>();
	        if (rowsUpdated > 0) {
	            res.put("message", "Prescription updated successfully.");
	            return ResponseEntity.ok(res);
	        } else {
	            res.put("message", "Appointment not found.");
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(res);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        Map<String, String> err = new HashMap<>();
	        err.put("message", "Error updating prescription: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
	    }
	}



}
