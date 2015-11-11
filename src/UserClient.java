import java.net.Socket;


public class UserClient {

	private final Socket socket;
	
	public UserClient(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		System.out.println("Running client!");
	}
	
}
