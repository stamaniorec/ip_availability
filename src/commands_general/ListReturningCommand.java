package commands_general;

import user.User;
import main.UserClient;




public abstract class ListReturningCommand extends Command {

	public ListReturningCommand(UserClient clientSession) {
		super(clientSession);
	}

	@Override
	public String execute(String[] args) {
		User user = clientSession.getUser();
		if (user == null) {
			return "error:notlogged";
		}
		
		return buildReturnString(args);
	}
	
	public abstract String buildReturnString(String[] args);

}
