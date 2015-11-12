import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
	
	private final int port;
	private ServerSocket serverSocket;
	private boolean isRunning;
	
	private Map<String, User> users;
	
	public Server(int port) {
		this.port = port;
		users = new HashMap<String, User>();
	}
	
	private void startServer() throws IOException {
		setRunning();
		serverSocket = new ServerSocket(port);
	}
	
	public void run() throws IOException {
		startServer();
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
	
	public synchronized void stopRunning() {
		if(!isRunning()) {
			throw new IllegalStateException("Already stopped running");
		}
		isRunning = false;
	}

	public synchronized void stopServer() throws IOException {
		serverSocket.close();
	}
	
	public Map<String, User> getUsers() { return users; }
	public User getUser(String username) { return users.get(username); }

}
