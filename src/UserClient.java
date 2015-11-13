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
	
	abstract class Command {
		protected UserClient clientSession;
		public Command(UserClient clientSession) {
			this.clientSession = clientSession;
		}
		public abstract String execute(String[] args);
	}
	
	class LoginCommand extends Command {
		public LoginCommand(UserClient clientSession) {
			super(clientSession);
		}
		@Override
		public String execute(String[] args) {
			String username = args[1];
			Server server = clientSession.getServer();
			User user1 = clientSession.getUser();
			User user = server.getUser(username);
			if(user == null) { // user has never logged in
				user = new User(username);
				server.getUsers().put(username, user);
			} else {
				// Log out from other session if such user is already logged in
				for(UserClient c : server.getClients()) {
					if(c != clientSession) {
						User u = c.getUser();
						if(u != null) {
							if(u.getUsername().equals(user.getUsername())) {
								new LogoutCommand(c).execute(new String[0]);
							}
						}
					}
				}
			}
			
			if(user != user1) { // already logged in, logging in with a different user
				if(user1 != null) 
					new LogoutCommand(clientSession).execute(new String[0]);
				user.login();
			}
			
//			user1 = user;
			clientSession.setUser(user);
//			clientSession.setUser(user1);
			return "ok";
		}
	}
	
	class LogoutCommand extends Command {

		public LogoutCommand(UserClient clientSession) {
			super(clientSession);
		}

		@Override
		public String execute(String[] args) {
			User user = clientSession.getUser();
			if(user == null) {
				return "error:notlogged";
			}
			user.logout();
			clientSession.setUser(null);
			return "ok";
		}
		
	}
	
	class ShutdownCommand extends Command {

		public ShutdownCommand(UserClient clientSession) {
			super(clientSession);
		}

		@Override
		public String execute(String[] args) {
			User user1 = clientSession.getUser();
			if(user1 == null) {
				return "error:notlogged";
			}
			return "ok";
		}
		
	}
	
	class ListAvailableCommand extends Command {

		public ListAvailableCommand(UserClient clientSession) {
			super(clientSession);
		}

		@Override
		public String execute(String[] args) {
			if(clientSession.getUser() == null) {
				return "error:notlogged";
			}
			String result = "ok";
			for(User user : clientSession.getServer().getUsers().values()) {
				if(user.isLoggedin()) result += (":" + user.getUsername());
			}
			return result;
		}
		
	}
	
	class ListAbsentCommand extends Command {

		public ListAbsentCommand(UserClient clientSession) {
			super(clientSession);
		}

		@Override
		public String execute(String[] args) {
			if(clientSession.getUser() == null) {
				return "error:notlogged";
			}
			String result = "ok";
			for(User user : clientSession.getServer().getUsers().values()) {
				if(!user.isLoggedin()) result += (":" + user.getUsername());
			}
			return result;
		}
		
	}
	
	class InfoCommand extends Command {

		public InfoCommand(UserClient clientSession) {
			super(clientSession);
		}

		@Override
		public String execute(String[] args) {
			User user = clientSession.getUser();
			if(user == null) {
				return "error:notlogged";
			}
			String username = args[1];
			User target = clientSession.getServer().getUser(username);
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
		
	}
	
	class CommandsExecuterFactory {
		private UserClient clientSession;
		public CommandsExecuterFactory(UserClient clientSession) {
			this.clientSession = clientSession;
		}
		public Command getCommand(String line) {
			String[] arr = line.split(":");
			if(arr.length > 2) return null;
			if(arr.length == 1) {
				if("shutdown".equals(arr[0])) {
					return new ShutdownCommand(clientSession);
				} else if("listavailable".equals(arr[0])) {
					return new ListAvailableCommand(clientSession);
				} else if("listabsent".equals(arr[0])) {
					return new ListAbsentCommand(clientSession);
				} else if("logout".equals(arr[0])) {
					return new LogoutCommand(clientSession);
				}
			} else if(arr.length == 2) {
				if("login".equals(arr[0])) {
					return new LoginCommand(clientSession);
				} else if("info".equals(arr[0])) {
					return new InfoCommand(clientSession);
				}
			}
			return null;
		}
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
				
				CommandsExecuterFactory f = new CommandsExecuterFactory(this);
				Command command = f.getCommand(line);
				if(command instanceof ShutdownCommand) {
					String output = command.execute(line.split(":"));
					if("ok".equals(output)) {
						server.stopRunning();
						break;
					} else {
						out.println(output);
					}
				} else {
					if(command != null) {
						out.println(command.execute(line.split(":")));
					} else {
						out.println("error:unknowncommand");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(in != null) in.close();
			if(out != null) out.close();
			System.out.println("Logging out from run");
			new LogoutCommand(this).execute(new String[0]); // if connection has fallen apart, logout automatically
		}
	}
	
	public synchronized User getUser() { return user; }
	public Socket getSocket() { return socket; }
	public Server getServer() { return server; }
	public void setUser(User u) { user = u; }
	
}
