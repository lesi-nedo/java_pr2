package errorPost;

public class NoPermissionException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public NoPermissionException(String mesg) {
		super(mesg);
	}
}
