package commands;
import commands_general.ListReturningCommand;

import user.User;
import main.UserClient;



public class ListAvailableCommand extends ListReturningCommand {

	public ListAvailableCommand(UserClient clientSession) {
		super(clientSession);
	}

	@Override
	public String buildReturnString(String[] args) {
		String result = "ok";
		for (User user : clientSession.getServer().getUsers().values()) {
			if (user.isLoggedIn())
				result += (":" + user.getUsername());
		}
		return result;
	}

}