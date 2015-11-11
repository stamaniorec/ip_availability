import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	private final int port;
	private ServerSocket serverSocket;
	private boolean isRunning;
	
	public Server(int port) {
		this.port = port;
	}
	
	public void startServer() throws IOException {
		setRunning();
		serverSocket = new ServerSocket(port);
		while(isRunning()) {
			final Socket clientSocket = serverSocket.accept();
			//TODO handle client
			clientSocket.close();
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
