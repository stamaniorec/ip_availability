class CommandsExecuterFactory {
//	private UserClient clientSession;

//	public CommandsExecuterFactory(UserClient clientSession) {
//		this.clientSession = clientSession;
//	}

	public static Command getCommand(String line, UserClient clientSession) {
		String[] arr = line.split(":");
		if (arr.length > 2)
			return null;
		if (arr.length == 1) {
			if ("shutdown".equals(arr[0])) {
				return new ShutdownCommand(clientSession);
			} else if ("listavailable".equals(arr[0])) {
				return new ListAvailableCommand(clientSession);
			} else if ("listabsent".equals(arr[0])) {
				return new ListAbsentCommand(clientSession);
			} else if ("logout".equals(arr[0])) {
				return new LogoutCommand(clientSession);
			}
		} else if (arr.length == 2) {
			if ("login".equals(arr[0])) {
				return new LoginCommand(clientSession);
			} else if ("info".equals(arr[0])) {
				return new InfoCommand(clientSession);
			}
		}
		return null;
	}
}