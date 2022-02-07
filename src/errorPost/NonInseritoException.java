package errorPost;

public class NonInseritoException extends Exception {
	private static final long serialVersionUID = 1L;
	public NonInseritoException (String mesg, Exception e) {
		super(mesg, e);
	}
}
