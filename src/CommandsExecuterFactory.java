import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

class CommandsExecuterFactory {
	
	private static Map<String, Class<?>> nameToCommandMap;
	
	static {
		nameToCommandMap = new HashMap<String, Class<?>>();
		nameToCommandMap.put("login", LoginCommand.class);
		nameToCommandMap.put("logout", LogoutCommand.class);
		nameToCommandMap.put("info", InfoCommand.class);
		nameToCommandMap.put("listavailable", ListAvailableCommand.class);
		nameToCommandMap.put("listabsent", ListAbsentCommand.class);
		nameToCommandMap.put("shutdown", ShutdownCommand.class);
	}
	
	public static Command getCommand(String inputLine, UserClient clientSession) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		String commandName = inputLine.split(":")[0];
		return (Command) nameToCommandMap.get(commandName).
				getConstructor(UserClient.class).newInstance(clientSession);
	}
	
}