
class LoginCommand extends Command {
	private Server server;
	private User sessionUser;
	
	public LoginCommand(UserClient clientSession) {
		super(clientSession);
		server = clientSession.getServer();
		sessionUser = clientSession.getUser();
	}
	
	@Override
	public String execute(String[] args) {
		String username = args[1];
		
		User userToLogin = server.getUser(username);
		if(userToLogin == null) {
			userToLogin = new User(username);
			server.getUsers().put(username, userToLogin);
		}

		if(userToLogin != sessionUser) {
			logoutFromOtherSessions(userToLogin.getUsername());
			logoutSessionUser(clientSession);
			userToLogin.login();
			clientSession.setUser(userToLogin);
		}
		
		return "ok";
	}
	
	private void logoutSessionUser(UserClient session) {
		new LogoutCommand(session).execute(new String[0]);
	}
	
	private void logoutFromOtherSessions(String username) {
		for (UserClient session : server.getClients()) {
			User sessionUser = session.getUser();
			if (sessionUser != null && sessionUser.getUsername().equals(username)) {
				logoutSessionUser(session);
			}
		}
	}
}