package SocialNetwork;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import post.Post;

public class Seguire {
	//Overview: La classe Seguire per mette di "seguire" i post ed e' immutable
	//AF: f(post) -> "Stai seguend" || "Non segui piu."
	//IR: post != NULL && who != NULL
	private final Post post; //post che segue
	private final Timestamp timestamp;
	private final  String who;
	//Requires: Post != NULL
	//Throws: NullPointerException
	//Effects: Inizializza la classe seguire
	public Seguire (Post post, String username) throws NullPointerException {
		if(post == null) new NullPointerException("Non puoi seguire un post nullo.");
		this.post = post;
		this.who = username;
	    this.timestamp = new Timestamp(System.currentTimeMillis());
	}
	
	//Effect: restituisce la data del post.
	public String getDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd.MM.yyyy 'At' hh:mm:ss");
		return dateFormat.format(timestamp);
	}
	
	//Effects: restituisce chi segue
	public String followedByWho() {
		return this.who;
	}
	//Effects: restituisce il post;
	public Post whatPost () {
		return this.post;
	}
	//Effects: restituisce chi a scritto il post.
	public String followee () {
		return post.getUser();
	}
	
	@Override
	public String toString () {
		return "SEGUACE:   Timestamp= " + getDate() + "; Follower= " + this.who + ";  " + post;
	}
}
