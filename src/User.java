
public class User {
	
	private String username;
	private boolean isLoggedIn;
	private int loginCount;
	
	public User(String username, boolean isLoggedIn) {
		this.username = username;
		this.isLoggedIn = isLoggedIn;
		loginCount = 0;
	}
	
}
