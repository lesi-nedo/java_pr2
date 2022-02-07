package  SocialNetwork;

import java.util.List;
import java.util.Map;
import java.util.Set;

import post.*;
import errorPost.*;
import errorNet.*;

public interface SocialNetInterface {
	//Overview: L'interfaccia per implementare il socialNetwork, e' mutable. E' composto da una Map per gestire
	//le persone seguite e la classe buildListOfPost che contiene tutti i post fatte dalla persona a.
	//Valori Entranti: <post, username, seguiti> dove il post e username e' di tipo String e seguiti puo essere di qualsiasi tipo
	
	
	//Requires: text != NULL username != NULL e della corretta forma.
	//Throws:NullPointerException, NonPuoiSeguireException, UserNameSbagliatoException
	//Effects: restituisce una stringa e segue il primo post trovato. null altrimenti.
	public String followWithText(String text, String username) throws NullPointerException, NonPuoiSeguireException, UserNameSbagliatoException;
	
	//Requires:  ID >= 0 && chiSegue != NULL
	//Throws: IllegalArgumentException, NonPuoiSeguireException
	//Effects: restituisce Una Stringa che dice "Stai seguendo questo post" se il post e' presente, altrimenti "Non trovato". 
	public String followWithId (int id, String username) throws  IllegalArgumentException, NonPuoiSeguireException, NullPointerException, UserNameSbagliatoException;
	
	
	//Requires: post != NULL
	//Throws: NullPointerException, NonPuoiSeguire
	//Effects: restituisce come nel caso followWithId
	public String followWithPost(Post post, String username) throws NullPointerException, NonPuoiSeguireException, UserNameSbagliatoException;
	
		
	//Requires: username != NULL
	//Throws: NullPointerException, UserNameSbagliatoException
	//Effects: restituisce tutti i post che l'username segue.
	public List<Post> seguiti (String username) throws NullPointerException, UserNameSbagliatoException;
	//Request ps != NULL
	//Throws: StaPerResettareException
	//Effects: Si ottiene la rete sociale derivata dalla list.
	public Map<String, Set<String>> guessFollowers(List<Post> ps);
	
	//restituisce tutti post
	public List<Post> getAllPosts();
	
	//Requires: socaial != NULL
	//Effects: restituisce gi utenti piu seguiti,  altirmenti NULL
	public List<String> influencers();
	
	//Effects: Restituisce l'insieme degli utenti menzionati nei posts, altrimenti NULL
	public Set<String> getMentionedUsers() throws UserNameSbagliatoException;
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
