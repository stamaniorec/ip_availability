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
			new LogoutCommand(this).execute(new String[0]); // if connection has fallen apart, logout automatically
		}
	}
	
	public synchronized User getUser() { return user; }
	public Socket getSocket() { return socket; }
	public Server getServer() { return server; }
	public void setUser(User u) { user = u; }
	
}
