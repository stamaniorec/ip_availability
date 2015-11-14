class ListAvailableCommand extends Command {

	public ListAvailableCommand(UserClient clientSession) {
		super(clientSession);
	}

	@Override
	public String execute(String[] args) {
		if (clientSession.getUser() == null) {
			return "error:notlogged";
		}
		
		String result = "ok";
		for (User user : clientSession.getServer().getUsers().values()) {
			if (user.isLoggedIn())
				result += (":" + user.getUsername());
		}
		return result;
	}

}