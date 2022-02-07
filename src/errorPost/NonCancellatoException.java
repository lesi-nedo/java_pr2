package errorPost;

public class NonCancellatoException extends Exception {
	private static final long serialVersionUID = 1L;

	public NonCancellatoException(String mesg) {
		super(mesg);
	}
}
