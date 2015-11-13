class InfoCommand extends Command {

	public InfoCommand(UserClient clientSession) {
		super(clientSession);
	}

	@Override
	public String execute(String[] args) {
		User user = clientSession.getUser();
		if (user == null) {
			return "error:notlogged";
		}
		String username = args[1];
		User target = clientSession.getServer().getUser(username);
		String result = "ok:";
		if (target != null) {
			result += (target.getUsername() + ":");
			result += (target.isLoggedin() + ":");
			result += (target.getLoginCount());
			for (Interval i : target.getSessionTimes()) {
				result += (":" + i.from());
				if (i.to() != null) {
					result += (":" + i.to());
				}
			}
		} else {
			result += (username + ":false:0");
		}
		return result;
	}

}