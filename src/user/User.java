package user;
import java.util.ArrayList;
import java.util.Date;


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
	
	public void login() {
		isLoggedIn = true; 
		++loginCount;
		session_times.add(new Interval(new Date()));
	}
	
	public void logout() {
		isLoggedIn = false;
		session_times.get(session_times.size()-1).setTo(new Date());
	}
	
	public boolean isLoggedIn() { return isLoggedIn; }
	public int getLoginCount() { return loginCount; }
	public void setLoginCount(int loginCount) { this.loginCount = loginCount; }
	public String getUsername() { return username; }
	public ArrayList<Interval> getSessionTimes() { return session_times; }
	
}
