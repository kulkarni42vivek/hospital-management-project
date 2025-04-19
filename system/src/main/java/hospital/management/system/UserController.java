package hospital.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.management.system.models.Users;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {
	
	private static final String URL = "jdbc:mysql://localhost:3306/hospital";
	private static final String USER = "root";
	private static final String PASSWORD = "1234";

	@PostMapping("createUser")
	public String createUser(@RequestBody Users user) {

	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	        
	        // 1. Check if user already exists
	        PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE userId = ?");
	        checkStmt.setString(1, user.getUserId());
	        ResultSet rs = checkStmt.executeQuery();
	        if (rs.next() && rs.getInt(1) > 0) {
	            return "User already exists.";
	        }

	        // 2. Insert new user
	        PreparedStatement stmt = conn.prepareStatement(
	            "INSERT INTO users (userId, password, role, firstName, lastName) VALUES (?, ?, ?, ?, ?)"
	        );
	        stmt.setString(1, user.getUserId());
	        stmt.setString(2, user.getPassword());
	        stmt.setString(3, "ADMIN");
	        stmt.setString(4, user.getFirstName());
	        stmt.setString(5, user.getLastName());

	        int rows = stmt.executeUpdate();
	        return rows > 0 ? "User created successfully!" : "Failed to create user.";

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error occurred while creating user.";
	    }
	}

	

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody Users request) {
	    Map<String, Object> response = new HashMap<>();

	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	        PreparedStatement stmt = conn.prepareStatement(
	                "SELECT * FROM users WHERE userId = ? AND password = ?"
	        );
	        stmt.setString(1, request.getUserId());
	        stmt.setString(2, request.getPassword());

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            Map<String, Object> userData = new HashMap<>();
	            userData.put("userId", rs.getString("userId"));
	            userData.put("firstName", rs.getString("firstName"));
	            userData.put("lastName", rs.getString("lastName"));
	            userData.put("role", rs.getString("role"));

	            response.put("user", userData);
	            response.put("message", "Login successful");
	            return ResponseEntity.ok(response);
	        } else {
	            response.put("message", "Invalid userId or password");
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("message", "Error occurred during login");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	

}
