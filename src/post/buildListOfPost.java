package post;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import errorPost.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class buildListOfPost implements BuildListOfPostInterface {
	//Overview: Crea una lista composta dai Post dati, ed e' mutable. 
	// AF: f(author, post) => list dove typeof list[i] == post, per ogni i;  
	//IR: List<Post> a.get(I) != NULL && List<Post> a !=NULL && idI == idJ => authorI == authorJ. per tutti I J appart.[0, tuttiPost.size());
	private AtomicInteger ID = new AtomicInteger(0); //NON MODIFICARE
	private LinkedList<Post> posts;
	private final Logger logger = LogManager.getLogger(buildListOfPost.class);
	//Effects: costruttore inizializza nella lista l'admin, cioe l'user con l'id 1 che e' l'unico che non puo essere cancellato.
	public buildListOfPost() {
		this.posts = new LinkedList<Post>();
		this.ID = new AtomicInteger(0);
		Post admin = null;
		try {
			admin = new Post(ID, "Admin", "");
		} catch (IllegalArgumentException | NullPointerException | NoPostException | UserNameSbagliatoException | LimiteSuperatoException e) {
			logger.error("Input incorretto.",e);
		}
		this.posts.addFirst(admin);
	}
	
	//Effects: restituisce la List<Post> 
	protected List<Post> getList(buildListOfPost toRet){
		return toRet.posts;
	}
	//Requires: "Username".matches("(?!)([a-zA-Z])(\\w+)").
	//Throws: UserNameSbagliatoException
	//Effects: restituisce al chimante la lista di strings che contiene tutti post pubblicati dalla persona, un List vuoto altrimenti
	public List<Post> writtenBy(List<Post> tuttiPost, String username) throws UserNameSbagliatoException, NullPointerException {
		if(username == null || tuttiPost == null || tuttiPost.isEmpty()) throw new NullPointerException("Inserire un username valido.");
		if(staticMethodsForPost.checkUserName("^[^a-zA-Z]", username) || staticMethodsForPost.checkUserName ("[^a-zA-Z0-9_]+", username)) throw new UserNameSbagliatoException("Username inserito non e' della corretta forma.");
		List<Post> valueToRet = new LinkedList<Post>();
		try {
			valueToRet = staticMethodsForPost.writtenBy(tuttiPost, username);
		} catch (NullPointerException e) {
			throw new NullPointerException("Il campo Username non puo essere vuoto.");
		}
		return valueToRet;
	}
	//Throws: NullPointerException.
	//Effects: restituisce tutte le persone menzionate, NULL altrimenti.
	public Set<String> getMentionedUsers(List<Post> tuttiPost) throws NullPointerException{
		if(tuttiPost.isEmpty() || tuttiPost == null) throw new NullPointerException("Argomento non puo essere vuoto.");
		try {
			return staticMethodsForPost.getMentionedUsers(tuttiPost); 
		} catch (NullPointerException e) {
			throw e;
		}
	}
	//Requires: username != NULL
	//Throws: UserNameSbagliatoException, NullPointerException
	//restituisce un Set di tutte le persone menzionate da username.
	public Set<String> getMentionedUsers(List<Post> tuttiPost, String username) throws UserNameSbagliatoException, NullPointerException{
		if(username == null) throw new NullPointerException("Inserire un username valido.");
		if(tuttiPost == null || tuttiPost.isEmpty()) throw new NullPointerException("Insieme dei post non puo essere nullo.");
		if(staticMethodsForPost.checkUserName("^[^a-zA-Z]", username) || staticMethodsForPost.checkUserName ("[^a-zA-Z0-9_]+", username)) throw new UserNameSbagliatoException("Username inserito non e' della corretta forma.");
		try {
			return staticMethodsForPost.getMentionedUsers(tuttiPost, username); 
		} catch (NullPointerException e) {
			throw e;
		}
	}
	//Requires: id >= 0
	//Throws: se id <= 0 IllegalArgumentException.
	//Effects: restituisce il post con l'id corretto , Null altrimenti
	public Post getById (List<Post> tuttiPost, int id) throws IllegalArgumentException{
		if (id < 0) throw new IllegalArgumentException("Id dev'essere un numero maggiore o uguale a zero.");
		if(tuttiPost == null || tuttiPost.isEmpty()) throw new NullPointerException("Oggetto e' vuoto");
		for(Post posts : tuttiPost) {
			if(posts.getId() == id) {
				return posts;
			}
		}
		return null;
	}
	//Requires: Username &&post != NULL
	//Throws: NonInseritoException, UserNameSbagliatoException, 
	//Modifies: this
	//Effects: inserisce nella lista il nuovo post creato dal user e delega il controllo per i valori NULL al dato post. 
	public Post insert (List<Post> tuttiPost, String username, String post) throws NonInseritoException{
		
		Post postToInsert;
		try {
			postToInsert = new Post(ID, username, post);
		} catch (IllegalArgumentException | NullPointerException | NoPostException | UserNameSbagliatoException | LimiteSuperatoException e) {
			System.out.println(e.getMessage());
			throw new NonInseritoException("Non e' stato possibile inserire il post", e);
		} 
		tuttiPost.add(postToInsert);
		return postToInsert;
	}
	//Requires: Username != NULL 
	//Throws: NullPointerException se username == NULL || username == admin && NonCancellatoException se non e' stato possibile cancellare;  
	//Modifies: this
	//Effects: cancella tutte le occorrenze dell'username nella lista. Rimuove i null dalla lista.
	public String deleteUsername (List<Post> tuttiPost, String username) throws NullPointerException, NonCancellatoException, NoPermissionException, UserNameSbagliatoException{
		if(username == null || tuttiPost == null || tuttiPost.isEmpty()) throw new NullPointerException("Inserire un username corretto o un oggetto non vuoto.");
		if(username.equals("Admin")) throw new NoPermissionException("Non e' possibile cancellare l'user Admin");
		if(staticMethodsForPost.checkUserName("^[^a-zA-Z]", username) || staticMethodsForPost.checkUserName ("[^a-zA-Z0-9_]+", username)) throw new UserNameSbagliatoException("Username inserito non e' della corretta forma.");
	    if(tuttiPost.removeIf(post -> post.getUser().equals(username))) {
			if(tuttiPost.contains(null)) throw new NullPointerException("Trovato un null nella lista tuttiPos.");
			return "Cancellato";
	    }	
		return "Non cancellato, probabilmente non esiste.";
	}
	
	//Rquires: username != NULL e che rispetti le convenzioni per l'username.
	//Throws: NullPointerException, UserNameSbagliatoException
	//Effects: restituisce tutte le occorrenze del username nella lista.
	public long getNumOcc (List<Post> tuttiPost, String username) throws NullPointerException {
		if(username == null || tuttiPost == null || tuttiPost.isEmpty())throw new NullPointerException("Inserire un username valido.");
		long occ = tuttiPost.stream().filter(post -> post.getUser().equals(username)).count();
		return occ;
	}
	
	//Effects: Copia l'oggetto List<Post>
	public List<Post> copy(List<Post> tuttiPost){
		return CopyOut.copy(tuttiPost);
	}

}
