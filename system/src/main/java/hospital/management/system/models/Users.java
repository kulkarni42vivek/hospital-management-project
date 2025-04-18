package hospital.management.system.models;

public class Users {
	private String userId;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    
    
    public Users(String userId, String password, String role, String firstName, String lastName) {
		super();
		this.userId = userId;
		this.password = password;
		this.role = role;
		this.firstName = firstName;
		this.lastName = lastName;
	}
    
	public Users() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}
