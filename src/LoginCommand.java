
class LoginCommand extends Command {
	Server server;
	User currentSessionUser;
	
	public LoginCommand(UserClient clientSession) {
		super(clientSession);
		server = clientSession.getServer();
		currentSessionUser = clientSession.getUser();
	}
	
	@Override
	public String execute(String[] args) {
		String username = args[1];
		User userToLogin = server.getUser(username);
		if(userToLogin == null) {
			userToLogin = new User(username);
			server.getUsers().put(username, userToLogin);
		} else {
			logoutFromOtherSessions(userToLogin.getUsername());			
		}
		if(userToLogin != currentSessionUser) {
			new LogoutCommand(clientSession).execute(new String[0]);
			userToLogin.login();
		}
		clientSession.setUser(userToLogin);
		return "ok";
	}
	
	private void logoutFromOtherSessions(String username) {
		for(UserClient session : server.getClients()) {
			if(session != clientSession) {
				User user = session.getUser();
				if(user != null && user.getUsername().equals(username)) {
					new LogoutCommand(session).execute(new String[0]);
				}
			}
		}
	}
}