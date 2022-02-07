package post;

import java.util.List;

public class CopyOut<E> {
	//Overview: Copia la classe
	private List<E> tuttiPost;
	public CopyOut(List<E> tutti) {
		this.tuttiPost = tutti;
	}
	public static <E> List<E> copy (List<E> all) {
		CopyOut<E> toRet =  new CopyOut<E> (all);
		return toRet.tuttiPost;
	}
}
