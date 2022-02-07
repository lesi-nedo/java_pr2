package SocialNetwork;

import java.util.Map;

public class copyMap<E> {
	private Map<String, E>  toCopy;
	public copyMap(Map<String, E> toCopy) {
		this.toCopy = toCopy;
	}
	public static <E> Map<String, E> copy(Map<String, E> cop){
		return new copyMap<E>(cop).toCopy;
	}
}
