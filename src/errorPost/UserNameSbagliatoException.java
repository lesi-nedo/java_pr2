package errorPost;

public class UserNameSbagliatoException extends Exception {
	private static final long serialVersionUID = -438381325783612500L;

	public UserNameSbagliatoException(String mesg) {
		super(mesg);
	}
}
