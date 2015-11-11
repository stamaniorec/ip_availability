
public class User {
	
	private String username;
	private boolean isLoggedIn;
	private int loginCount;
	
	public User(String username) {
		this.username = username;
		isLoggedIn = false;
		loginCount = 0;
	}
	
	public void login() { isLoggedIn = true; ++loginCount;}
	public void logout() { isLoggedIn = false; }
	public boolean isLoggedin() { return isLoggedIn; }
	public int getLoginCount() { return loginCount; }
	public void setLoginCount(int loginCount) { this.loginCount = loginCount; }
	public String getUsername() { return username; }
	
}
