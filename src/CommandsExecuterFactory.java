class CommandsExecuterFactory {
	
	public static Command getCommand(String inputLine, UserClient clientSession) {
		String commandName = inputLine.split(":")[0];
		if ("shutdown".equals(commandName)) {
			return new ShutdownCommand(clientSession);
		} else if ("listavailable".equals(commandName)) {
			return new ListAvailableCommand(clientSession);
		} else if ("listabsent".equals(commandName)) {
			return new ListAbsentCommand(clientSession);
		} else if ("logout".equals(commandName)) {
			return new LogoutCommand(clientSession);
		} else if ("login".equals(commandName)) {
			return new LoginCommand(clientSession);
		} else if ("info".equals(commandName)) {
			return new InfoCommand(clientSession);
		}
		return null;
	}
	
}