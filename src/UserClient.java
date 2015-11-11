import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class UserClient {

	private final Server server;
	private final Socket socket;
	
	public UserClient(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
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
					String output = shutdown(arr);
					if("ok".equals(output)) {
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
		String username = args[0];
		server.getCurrentlyLoggedUsers().add(username);
		int numTimesLoggingIn = 0;
		if(server.getUsersToLoginCount().containsKey(username)) {
			numTimesLoggingIn = server.getUsersToLoginCount().get(username);
		}
		server.getUsersToLoginCount().put(username, numTimesLoggingIn+1);
		return "ok";
	}
	
	public String logout(String[] args) {
		String username = args[0];
		return server.getCurrentlyLoggedUsers().remove(username) ? "ok" : "error:notlogged";
	}
	
	public String info(String[] args) {
		String username = args[0];
		String username2 = args[2];
		if(server.getCurrentlyLoggedUsers().contains(username)) {
			String result = "ok:";
			result += (username2 + ":");
			result += (server.getCurrentlyLoggedUsers().contains(username2) + ":");
			int count = 0;
			if(server.getUsersToLoginCount().containsKey(username2)) {
				count = server.getUsersToLoginCount().get(username2);
			}
			result += count;
			return result;
		} else {
			return "error:notlogged";
		}
	}
	
	public String listavailable(String[] args) {
		String username = args[0];
		if(server.getCurrentlyLoggedUsers().contains(username)) {
			String result = "ok";
			for(String user : server.getCurrentlyLoggedUsers()) {
				result += (":" + user);
			}
			return result;
		} else {
			return "error:notlogged";
		}
	}
	
	public String shutdown(String[] args) {
		String username = args[0];
		if(server.getCurrentlyLoggedUsers().contains(username)) {
			return "ok";
		} else {
			return "error:notlogged";
		}
	}
	
}
