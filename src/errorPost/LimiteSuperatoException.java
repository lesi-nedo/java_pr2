package errorPost;

public class LimiteSuperatoException extends Exception {
	private static final long serialVersionUID = 3696213397509717314L;
	public LimiteSuperatoException(String mesg) {
		super(mesg);
	}
}
