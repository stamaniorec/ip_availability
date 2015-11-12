import java.util.ArrayList;


public class User {
	
	private String username;
	private boolean isLoggedIn;
	private int loginCount;
	private ArrayList<Interval> session_times;
	
	public User(String username) {
		this.username = username;
		isLoggedIn = false;
		loginCount = 0;
		session_times = new ArrayList<Interval>();
	}
	
	public void login() { isLoggedIn = true; ++loginCount;}
	public void logout() { isLoggedIn = false; }
	public boolean isLoggedin() { return isLoggedIn; }
	public int getLoginCount() { return loginCount; }
	public void setLoginCount(int loginCount) { this.loginCount = loginCount; }
	public String getUsername() { return username; }
	
}
