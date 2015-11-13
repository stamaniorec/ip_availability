
class LoginCommand extends Command {
	public LoginCommand(UserClient clientSession) {
		super(clientSession);
	}
	@Override
	public String execute(String[] args) {
		String username = args[1];
		Server server = clientSession.getServer();
		User user1 = clientSession.getUser();
		User user = server.getUser(username);
		if(user == null) { // user has never logged in
			user = new User(username);
			server.getUsers().put(username, user);
		} else {
			// Log out from other session if such user is already logged in
			for(UserClient c : server.getClients()) {
				if(c != clientSession) {
					User u = c.getUser();
					if(u != null) {
						if(u.getUsername().equals(user.getUsername())) {
							new LogoutCommand(c).execute(new String[0]);
						}
					}
				}
			}
		}
		
		if(user != user1) { // already logged in, logging in with a different user
			if(user1 != null) 
				new LogoutCommand(clientSession).execute(new String[0]);
			user.login();
		}
		
		clientSession.setUser(user);
		return "ok";
	}
}