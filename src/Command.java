abstract class Command {
	protected UserClient clientSession;

	public Command(UserClient clientSession) {
		this.clientSession = clientSession;
	}
	
	public abstract String execute(String[] args);
}