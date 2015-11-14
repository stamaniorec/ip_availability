package commands;
import commands_general.ListReturningCommand;

import user.User;
import main.UserClient;



public class ListAbsentCommand extends ListReturningCommand {

	public ListAbsentCommand(UserClient clientSession) {
		super(clientSession);
	}

	@Override
	public String buildReturnString(String[] args) {
		String result = "ok";
		for (User user : clientSession.getServer().getUsers().values()) {
			if (!user.isLoggedIn())
				result += (":" + user.getUsername());
		}
		return result;
	}

}