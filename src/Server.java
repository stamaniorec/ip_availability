import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
	
	private final int port;
	private ServerSocket serverSocket;
	private boolean isRunning;
	
	private Map<String, User> users;
	private List<Socket> clientSockets;
	
	public Server(int port) {
		this.port = port;
		users = Collections.synchronizedMap(new HashMap<String, User>());
		clientSockets = Collections.synchronizedList(new ArrayList<Socket>());
	}
	
	private void startServer() throws IOException {
		setRunning();
		serverSocket = new ServerSocket(port);
	}
	
	public void run() throws IOException {
		startServer();
		while(isRunning()) {
			try {
				final Socket clientSocket = serverSocket.accept();
				new Thread(new UserClient(this, clientSocket)).start();
				clientSockets.add(clientSocket);
			} catch(SocketException ex) { break; }
		}
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
	
	public synchronized void stopRunning() throws IOException {
		if(!isRunning()) {
			throw new IllegalStateException("Already stopped running");
		}
		isRunning = false;
		stopServer();
	}

	public synchronized void stopServer() throws IOException {
		for(Socket clientSocket : clientSockets) {
			if(!clientSocket.isClosed()) clientSocket.close();
		}
		serverSocket.close();
	}
	
	public Map<String, User> getUsers() { return users; }
	public User getUser(String username) { return users.get(username); }

}
