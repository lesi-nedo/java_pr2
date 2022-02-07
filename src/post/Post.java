package post;

import java.text.SimpleDateFormat;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import errorPost.*;

public final class Post implements postInterface, Comparable<Post>{
	//Overview: un dato di tipo Nodo non modificabile con un id, testo del post, l'autore e persone citate nel post con un #, autore puo avere solo un 
	//nome singolo del tipo user name. Dopo # ci dev'essere un user name.
	//AF: f(author, text) => "Nodo"
	//IR: author != NULL && text != NULL && post <= 140 carat  && author.matches("(?!)([a-zA-Z])(\\w+)_?").
	private final int id;
	private final Timestamp timestamp;// nel constructor
	private final String author;
	private final String text;
	private Hashtable<String, Integer> taggedUsers = null;
	//Requires: id >= 0 && author != NULL && text = NULL && text <= 140 caratt && author.matches("(?!)([a-zA-Z])(\\w+)").
	//Throws: IllegalArgumentException se id < 0 && NullPointerException se text o author == NULL && LimiteSuperatoException
	//Modifies: this
	//Effects: inizializza le variabili della classe, e controlla se il nome dell'user comincia con una lettera e dopo continua con lettere e/o numeri.
	public Post (AtomicInteger id, String author, String text ) throws IllegalArgumentException, NullPointerException, NoPostException, LimiteSuperatoException, UserNameSbagliatoException {
		Objects.requireNonNull(id, "Id non puo' essere nullo");
		if(id.intValue() < 0) throw new IllegalArgumentException("Id dev'essere un numero naturale");
		Objects.requireNonNull(author, "Autore non puo' essere null.");
		Objects.requireNonNull(text, "Il post non puo' essere null");
		//COntrollo Username 
		if(staticMethodsForPost.checkUserName("^[^a-zA-Z]", author) || staticMethodsForPost.checkUserName("[^a-zA-Z0-9_]+", author)) throw new UserNameSbagliatoException("Username inserito non e' corretto.");
		if(text.length() > postInterface.MAX_LENGTH) throw new LimiteSuperatoException("Superato il limite di caratteri.");
		this.id = id.incrementAndGet();
		this.timestamp = new Timestamp(System.currentTimeMillis());
		this.author = author;
		this.text = text;
		taggedUsers = new Hashtable<String, Integer>();
		addTaggedUser(text); //se ci sono persone menzionate gli trova e gli inserisce nella Hashtable. 
	}
	//Effect: restituisce la data del post.
	public String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd.MM.yyyy 'At' hh:mm:ss");
		return dateFormat.format(timestamp);
	}
	//Effect: ritorna l'id dell'user.
	public int getId() {
		return this.id;
	}
	//Effect: ritorna il nome dell'autore
	public String getUser() {
		return this.author;
	}
	//Effect: ritorna il post scritto dall'user.
	public String getPost() {
		return this.text;
	}
	//Effect: hashtable con tutte le persone menzionate, vuoto altrimenti
	public Map<String, Integer> getTagged(){
		if(taggedUsers.isEmpty() || taggedUsers == null) return new Hashtable<String, Integer>();	
		return  Collections.unmodifiableMap(taggedUsers);
	}

	//Modifies: taggedPeople
	//Effects: aggiunge tutte le persone nella Hashtable menzionate nei post dopo #, un user e' menzionato <=> dopo # c'e un carattere, seguito da altri caratteri o numeri.
	private void addTaggedUser(String text) {
			Pattern pattern = Pattern.compile("#([a-z])(\\w+)?(_?)", Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(text);
			while(matcher.find()) {
				String toInsert = text.substring(matcher.start()+1, matcher.end());
				if(taggedUsers.putIfAbsent(toInsert, 1) != null) taggedUsers.computeIfPresent(toInsert, (key, value) -> value + 1);
			}

	}
	public boolean isEmpty() {
		return id == 0 || author.isEmpty() ||text.isEmpty();
	}
	@Override 
	public int compareTo (Post ps) {
		return this.id - ps.getId();
	}
	@Override
	public boolean equals (Object o) {
		if (this == o) return true;
		if(o == null || this.getClass() != o.getClass()) return false;
		Post newPos = (Post) o;
		return this.id == newPos.getId();
	}
	@Override
	public int hashCode() {
		return Objects.hash(this.id);
	}
	@Override
	public String toString () {
		return "POST:  " + " id= " +this.id + "; TimpeStamp= " + getDate() + "; Author= " + this.author + "; Text= " + this.text + "; TaggedUsers=" + this.taggedUsers.toString() + "\n";
	}
}

