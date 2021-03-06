package commands;
import commands_general.Command;

import user.User;
import main.UserClient;



public class ShutdownCommand extends Command {

	public ShutdownCommand(UserClient clientSession) {
		super(clientSession);
	}

	@Override
	public String execute(String[] args) {
		User user1 = clientSession.getUser();
		if (user1 == null) {
			return "error:notlogged";
		}
		return "ok";
	}

}