import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
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
				if("login".equals(arr[1])) {
					out.println(login(arr));
				} else if("logout".equals(arr[1])) {
					out.println(logout(arr));
				} else if("info".equals(arr[1])) {
					out.println(info(arr));
				} else if("listavailable".equals(arr[1])) {
					out.println(listavailable(arr));
				} else if("shutdown".equals(arr[1])) {
//					String output = shutdown(arr);
//					if("ok".equals(output)) {
//						break;
//					} else {
//						out.println(output);
//					}
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
		String username = args[0];
		User user = server.getUser(username);
		if(user == null) {
			user = new User(username);
			server.getUsers().add(user);
		}
		user.login();
		this.user = user;
		return "ok";
	}
	
	public String logout(String[] args) {
		String username = args[0];
		User user = server.getUser(username);
		if(!user.isLoggedin()) return "error:notlogged";
		user.logout();
		return "ok";
	}
	
	public String info(String[] args) {
		String username = args[0];
		String username2 = args[2];
		User executer = server.getUser(username);
		User target = server.getUser(username2);
		if(executer != null && executer.isLoggedin() && target != null) {
			String result = "ok:";
			result += (target.getUsername() + ":");
			result += (target.isLoggedin() + ":");
			result += (target.getLoginCount());
			return result;
		} else {
			return "error:notlogged";
		}
	}

	public String listavailable(String[] args) {
		String username = args[0];
		if(server.getUser(username) == null)
			return "error:notlogged";
		String result = "ok";
		for(User user : server.getUsers()) {
			if(user.isLoggedin()) result += (":" + user.getUsername());
		}
		return result;
	}
//	
//	public String shutdown(String[] args) {
//		String username = args[0];
//		if(server.getCurrentlyLoggedUsers().contains(username)) {
//			return "ok";
//		} else {
//			return "error:notlogged";
//		}
//	}
	
}
