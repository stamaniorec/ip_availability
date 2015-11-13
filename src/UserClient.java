import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class UserClient implements Runnable {

	private final Server server;
	private final Socket socket;
	private User user;
	
	public UserClient(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		user = null;
	}
	
	@Override
	public void run() {
		PrintStream out = null;
		Scanner in = null;
		try {
			out = new PrintStream(socket.getOutputStream());
			in = new Scanner(socket.getInputStream());
			
			out.println("въведете команда: ");
			
			while(in.hasNextLine()) {
				String line = in.nextLine();
				
				String[] arr = line.split(":");
				if(arr.length >= 2) {
					if("login".equals(arr[0])) {
						out.println(login(arr));
					} else if("info".equals(arr[0])) {
						out.println(info(arr));
					} else {
						out.println("error:unknowncommand");
					}
				} else if(arr.length == 1) {
					if("logout".equals(arr[0])) {
						out.println(logout(arr));
					} else if("listavailable".equals(arr[0])) {
						out.println(listavailable(arr));
					} else if("shutdown".equals(arr[0])) {
						String output = shutdown(arr);
						if("ok".equals(output)) {
							server.stopRunning();
							break;
						} else {
							out.println(output);
						}
					} else {
						out.println("error:unknowncommand");
					}
				} else {
					out.println("error:unknowncommand");
				}
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in != null) in.close();
			if(out != null) out.close();
			logout(new String[0]); // if connection has fallen apart, logout automatically
		}
	}
	
	public synchronized String login(String[] args) {
		String username = args[1];
		User user = server.getUser(username);
		if(user == null) { // user has never logged in
			user = new User(username);
			server.getUsers().put(username, user);
		} else {
			// Log out from other session if such user is already logged in
			for(UserClient c : server.getClients()) {
				if(c != this) {
					User u = c.getUser();
					if(u != null) {
						if(u.getUsername().equals(user.getUsername())) {
							c.logout(new String[]{ c.getUser().getUsername() });
						}
					}
				}
			}
		}
		
		if(user != this.user) { // already logged in, logging in with a different user
			if(this.user != null) logout(new String[]{ this.user.getUsername() });
			user.login();
		}
		
		this.user = user;
		return "ok";
	}
	
	public synchronized String logout(String[] args) {
		if(this.user == null) {
			return "error:notlogged";
		}
		user.logout();
		this.user = null;
		return "ok";
	}
	
	public synchronized String info(String[] args) {
		if(this.user == null) {
			return "error:notlogged";
		}
		String username = args[1];
		User target = server.getUser(username);
		String result = "ok:";
		if(target != null) {
			result += (target.getUsername() + ":");
			result += (target.isLoggedin() + ":");
			result += (target.getLoginCount());
			for(Interval i : target.getSessionTimes()) {
				result += (":" + i.from());
				if(i.to() != null) {
					result += (":" + i.to());
				}
			}
		} else {
			result += (username + ":false:0");
		}
		return result;
	}

	public synchronized String listavailable(String[] args) {
		if(this.user == null) {
			return "error:notlogged";
		}
		String result = "ok";
		for(User user : server.getUsers().values()) {
			if(user.isLoggedin()) result += (":" + user.getUsername());
		}
		return result;
	}
	
	public synchronized String shutdown(String[] args) {
		if(this.user == null) {
			return "error:notlogged";
		}
		return "ok";
	}
	
	public synchronized User getUser() { return user; }
	public Socket getSocket() { return socket; }
	
}
