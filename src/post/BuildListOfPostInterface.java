package post;

import java.util.List;

import errorPost.*;

public interface BuildListOfPostInterface {
	//Overview: Interfaccia per la classe che crea una Lista mutable di post non modificabili 	
	
	//Requires: username != NULL e che rispetti le convenzioni.
	//Throws: NullPointerException, UserNameSbagliatoException
	//Effects: restituisce tutte le occorrenze del username nella lista.
	public long getNumOcc (List<Post> tuttiPost, String username);
	//Requires: id >= 0
	//Throws: se id <= 0 IllegalArgumentException.
	//Effects: restituisce il dato Post, NuLL altrimenti
	public Post getById (List<Post> tuttiPost, int id) throws IllegalArgumentException;
	//Throws: NonInseritoException.
	//Modifies: this
	//Effects: inserisce nella lista il nuovo post creato dal user. I controlli fa il dato post.
	public Post insert (List<Post> tuttiPost, String username, String post) throws NonInseritoException;
	//Effects: copia l'oggetto List<Post>
	public List<Post> copy(List<Post> tuttiPost);
	//Requires: Username != NULL
	//Throws: NullPointerException se username == NULL || username == "admin" && NonCancellatoException se non e' stato possibile cancellare; 
	//Modifies: this
	//Effects: cancella tutte le occorrenze dell'username nella lista. Rimuove  null dalla lista
	public String deleteUsername (List<Post> tuttiPost, String username) throws NullPointerException, NonCancellatoException, NoPermissionException, UserNameSbagliatoException; 
}
