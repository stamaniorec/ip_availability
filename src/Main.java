import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class Main {

	private static Set<String> currentlyLoggedUsers = new HashSet<String>();
	private static HashMap<String, Integer> usersToLoginCount = new HashMap<String, Integer>();
	
	public static String login(String[] args) {
		String username = args[0];
		currentlyLoggedUsers.add(username);
		int numTimesLoggingIn = 0;
		if(usersToLoginCount.containsKey(username)) {
			numTimesLoggingIn = usersToLoginCount.get(username);
		}
		usersToLoginCount.put(username, numTimesLoggingIn+1);
		return "ok";
	}
	
	public static String logout(String[] args) {
		String username = args[0];
		return currentlyLoggedUsers.remove(username) ? "ok" : "error:notlogged";
	}
	
	public static String info(String[] args) {
		String username = args[0];
		String username2 = args[2];
		if(currentlyLoggedUsers.contains(username)) {
			String result = "ok:";
			result += (username2 + ":");
			result += (currentlyLoggedUsers.contains(username2) + ":");
			int count = 0;
			if(usersToLoginCount.containsKey(username2)) {
				count = usersToLoginCount.get(username2);
			}
			result += count;
			return result;
		} else {
			return "error:notlogged";
		}
	}
	
	public static String listavailable(String[] args) {
		String username = args[0];
		if(currentlyLoggedUsers.contains(username)) {
			String result = "ok";
			for(String user : currentlyLoggedUsers) {
				result += (":" + user);
			}
			return result;
		} else {
			return "error:notlogged";
		}
	}
	
	public static String shutdown(String[] args) {
		String username = args[0];
		if(currentlyLoggedUsers.contains(username)) {
			return "ok";
		} else {
			return "error:notlogged";
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket s = new Socket("127.0.0.1", 31111);
		final PrintStream out = new PrintStream(s.getOutputStream());
		Scanner input = new Scanner(s.getInputStream());
		while(true) {
			out.println("въведете команда: ");
			String line = input.nextLine();

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
		input.close();
		s.close();
	}	
}