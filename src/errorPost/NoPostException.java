package errorPost;

public class NoPostException extends Exception {
	private static final long serialVersionUID = 2L;
	public NoPostException (String mesg) {
		super(mesg);
	}
}
