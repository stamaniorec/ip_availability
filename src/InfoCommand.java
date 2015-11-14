class InfoCommand extends ListReturningCommand {

	public InfoCommand(UserClient clientSession) {
		super(clientSession);
	}

	@Override
	public String buildReturnString(String[] args) {
		String username = args[1];
		User target = clientSession.getServer().getUser(username);

		if(target == null) {
			return "ok:" + username + ":false:0";
		}
		
		return buildUserString(target);
	}
	
	private String buildUserString(User user) {
		String result = String.format("ok:%s:%b:%d", 
			user.getUsername(),
			user.isLoggedIn(),
			user.getLoginCount()
		);
		
		for (Interval i : user.getSessionTimes()) {
			result += (":" + i.from());
			if (i.to() != null) {
				result += (":" + i.to());
			}
		}

		return result;
	}

}