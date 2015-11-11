import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	private final int port;
	private ServerSocket serverSocket;
	private boolean isRunning;
	
	private ArrayList<User> users;
	
	public Server(int port) {
		this.port = port;
		users = new ArrayList<User>();
	}
	
	public void startServer() throws IOException {
		setRunning();
		serverSocket = new ServerSocket(port);
		while(isRunning()) {
			final Socket clientSocket = serverSocket.accept();
			new UserClient(this, clientSocket).run();
		}
		stopServer();
	}
	
	private synchronized void setRunning() {
		if(isRunning()) {
			throw new IllegalStateException("Already running");
		}
		isRunning = true;
	}
	
	private synchronized boolean isRunning() {
		return isRunning;
	}

	public synchronized void stopServer() throws IOException {
		serverSocket.close();
	}
	
	public void loginUser(User user) {
		System.out.println(user.getUsername());
		if(!users.contains(user)) users.add(user);
		user.login();
	}

}
