package hospital.management.system;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hospital.management.system.models.Users;

@RestController
@RequestMapping("/api/user")
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
	        stmt.setString(3, user.getRole());
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
	public String authenticateUser(@RequestBody Users request) {
	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	        PreparedStatement stmt = conn.prepareStatement(
	                "SELECT * FROM users WHERE userId = ? AND password = ?"
	        );
	        stmt.setString(1, request.getUserId());
	        stmt.setString(2, request.getPassword());

	        ResultSet rs = stmt.executeQuery();
	        if (rs.next()) {
	            return " Login successful. Welcome, " + rs.getString("firstName") + "!";
	        } else {
	            return " Invalid userId or password.";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "Error occurred during login.";
	    }
	}
	
	@GetMapping("getUserData/{userId}")
	public Users getUserById(@PathVariable int userId) {
	    Users user = null;
	    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
	        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE userId = ?");
	        stmt.setInt(1, userId);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            user = new Users(
	                rs.getString("userId"),
	                rs.getString("password"),      
	                rs.getString("role"),
	                rs.getString("firstName"),
	                rs.getString("lastName")
	            );
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return user;
	}

}
