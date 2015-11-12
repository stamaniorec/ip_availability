import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class UserClient {

	private final Server server;
	private final Socket socket;
	private User user;
	
	public UserClient(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		user = null;
	}
	
	public void run() throws IOException {
		final PrintStream out = new PrintStream(socket.getOutputStream());
		final Scanner in = new Scanner(socket.getInputStream());
		
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
		
		in.close();
		out.close();

	}
	
	public String login(String[] args) {
		String username = args[1];
		User user = server.getUser(username);
		if(user == null) {
			user = new User(username);
			server.getUsers().put(username, user);
		}
		if(!user.isLoggedin()) user.login();
		this.user = user;
		return "ok";
	}
	
	public String logout(String[] args) {
		if(this.user == null) {
			return "error:notlogged";
		}
		user.logout();
		this.user = null;
		return "ok";
	}
	
	public String info(String[] args) {
		if(this.user == null) {
			return "error:notlogged";
		}
		String username = args[1];
		User target = server.getUser(username);
		if(target != null) {
			String result = "ok:";
			result += (target.getUsername() + ":");
			result += (target.isLoggedin() + ":");
			result += (target.getLoginCount());
			for(Interval i : target.getSessionTimes()) {
				result += (":" + i.from());
				if(i.to() != null) {
					result += (":" + i.to());
				}
			}
			return result;
		} else {
			return "error:notlogged";
		}
	}

	public String listavailable(String[] args) {
		String username = this.user.getUsername();
		if(server.getUser(username) == null)
			return "error:notlogged";
		String result = "ok";
		for(User user : server.getUsers().values()) {
			if(user.isLoggedin()) result += (":" + user.getUsername());
		}
		return result;
	}
	
	public String shutdown(String[] args) {
		if(this.user == null) {
			return "error:notlogged";
		}
		return "ok";
	}
	
}
