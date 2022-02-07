package post;

import java.util.*;

public interface postInterface {
	//Overview: un dato di tipo Nodo non modificabile con un id, testo del post, l'autore e persone citate nel post con un #, autore puo avere solo un 
	//nome singolo del tipo user name. Dopo # ci dev'essere un user name.
	//i valori in entrata: <id, username, post> -> implementa diversi metodi per lavorare sul tipo di dato.
	static final int MAX_LENGTH = 140;
	//Effect: ritorna l'id dell'user.
	public int getId ();
	//Effect: ritorna il nome dell'autore
	public String getUser();
	//Effect: ritorna il post scritto dall'user.
	public String getPost();
	//Effect: restituisce la data del post.
	public String getDate();
	//Effect: hashSet con tutte le persone menzionate, NUll altrimenti
	public Map<String, Integer> getTagged(); 
	//Effects: controlla se il Posr e'vuouto.
	public boolean isEmpty();
}
