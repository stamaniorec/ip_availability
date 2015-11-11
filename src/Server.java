import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class Server {
	
	private final int port;
	private ServerSocket serverSocket;
	private boolean isRunning;
	
	private Set<String> currentlyLoggedUsers;
	private HashMap<String, Integer> usersToLoginCount;
	
	public Set<String> getCurrentlyLoggedUsers() {
		return currentlyLoggedUsers;
	}
	
	public HashMap<String, Integer> getUsersToLoginCount() {
		return usersToLoginCount;
	}
	
	public Server(int port) {
		this.port = port;
		currentlyLoggedUsers = new HashSet<String>();
		usersToLoginCount = new HashMap<String, Integer>();
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

}
