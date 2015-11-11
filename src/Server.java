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
	
	public ArrayList<User> getUsers() { return users; }
	public User getUser(String username) {
		for(User user : users) {
			if(user.getUsername().equals(username))
				return user;
		}
		return null;
	}
}
