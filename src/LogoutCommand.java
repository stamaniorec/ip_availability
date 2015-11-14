class LogoutCommand extends Command {

	public LogoutCommand(UserClient clientSession) {
		super(clientSession);
	}
	
	@Override
	public String execute(String[] args) {
		User user = clientSession.getUser();
		if(user == null) {
			return "error:notlogged";
		}

		user.logout();
		clientSession.setUser(null);

		return "ok";
	}
	
}