import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class UserClient {

	private final Socket socket;
	
	public UserClient(Socket socket) {
		this.socket = socket;
	}
	
	public void run() throws IOException {
		final PrintStream out = new PrintStream(socket.getOutputStream());
		final Scanner in = new Scanner(socket.getInputStream());
		
		while(in.hasNextLine()) {
			String input = in.nextLine();
			System.out.println(input);
		}
		
		in.close();
		out.close();
	}
	
}
