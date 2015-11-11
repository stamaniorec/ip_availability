import java.io.IOException;



public class Main {
	
	private static final int SERVER_PORT = 31111;

	public static void main(String[] args) throws IOException {
		new Server(SERVER_PORT).startServer();
	}
	
}