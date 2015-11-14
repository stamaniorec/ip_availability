package main;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import user.User;

import commands.LogoutCommand;
import commands.ShutdownCommand;
import commands_general.Command;
import commands_general.CommandsExecuterFactory;


public class UserClient implements Runnable {

	private final Server server;
	private final Socket socket;
	private User user;
	private Scanner in;
	private PrintStream out;
	
	public UserClient(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.user = null;
	}
	
	@Override
	public void run() {
		try {
			initIO();
			out.println("въведете команда: ");
			
			while(in.hasNextLine()) {
				parse(in.nextLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}
	
	private void initIO() throws IOException {
		out = new PrintStream(socket.getOutputStream());
		in = new Scanner(socket.getInputStream());
	}
	
	private void parse(String line) throws IOException {
		Command command;
		try {
			command = CommandsExecuterFactory.getCommand(line, this);
			String output = command.execute(line.split(":"));
			if(command instanceof ShutdownCommand && "ok".equals(output)) {
				server.stopRunning();
			}
			out.println(output);
		} catch (Exception e) {
			out.println("error:unknowncommand");
		}
	}
	
	private void cleanup() {
		if(in != null) in.close();
		if(out != null) out.close();
		// if connection has fallen apart, logout automatically
		new LogoutCommand(this).execute(new String[0]);
	}
	
	public synchronized User getUser() { return user; }
	public void setUser(User u) { user = u; }
	
	public Socket getSocket() { return socket; }
	public Server getServer() { return server; }
	
}
