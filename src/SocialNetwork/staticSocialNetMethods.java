package SocialNetwork;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import post.*;
public class staticSocialNetMethods {
	//Overview: Un insieme dei metodo statici per lavorare sul SocialNetwork
	//AF: f(social, ps) -> Top influencer &&  (a(buildListOfPost) -> ReteSociale)
    //IR: ps != NULL && followers != NULL && ps[i] != NULL 
	
	
	//Requires: ps != NULL
	//Throws: NullPointerException.
	//Effects: Restituisce la Social Network derivata dalla classe BUildListOfPost
	//se un username1 ha menzionato in un post un'altro username2 allora username1 segue username2,
	//un username segue un post allora segue anche il suo autotre. Attenzione social value puo essere VUOTO. Se un user viene menzionato allora
	//automaticamente fa parte della Social Network
	public static Map<String, Set<String>> guessFollowers(Map<String, Set<String>> social, List<Post> ps) throws NullPointerException {
		if(ps == null || social == null || ps.isEmpty() || social.isEmpty()) throw new NullPointerException ("La lista di pos non puo essere vuota");
		if(ps.contains(null)) throw new NullPointerException("Attenzione un elemento nella lista e' vuoto.");
		ps.forEach(post -> {
			Set<String> toInser = new HashSet<String>(post.getTagged().keySet());
			if(toInser != null || !toInser.isEmpty()) {
				helpGuess(post.getUser(), toInser, social);
			}
		});
		return social;
	}
	//Requires: toInser != NULL
	//Effects: method per evitare tanti annidamenti cioe' la callback hell.
	private static void helpGuess(String user, Set<String> toInser, Map<String, Set<String>> social) {
		toInser.forEach(person -> {
			//questo ci assicura che se l'user si menziona da solo non finisca nel Set<String> che rappresenta le persone seguite.
			if(!person.equals(user)) social.compute(user, (k, v) -> {
				if(v == null || v.isEmpty()) {
					v = new HashSet<String>();
					v.add(person);
					return v;
				}
				v.add(person); 
				return v;
			});
			social.putIfAbsent(person, new HashSet<String>());
		});
	}
	//Overview: followers != null 
	//Throws: NullPointerException
	//Effects: restituisce la lista dei "influencer"
	public static List<String> influencers(Map<String, Set<String>> followers){
		if(followers == null) throw new NullPointerException();
		Hashtable<String, Integer> allUsers = new Hashtable<String, Integer>(); //qua vanno tutte le persone che sono state menzionate o se il loro post e' seguito. //menzionamento da un user nuovo o seguitore nuove vale +1
		long toBeInflue = Math.round((followers.size()*13)/100);				//per essere un influencer il numero totale dei menzionamenti e persone che seguono deve essere >= //il 13% dei users totali della social network.
		List<String> infl = new ArrayList<String>();
		followers.forEach((key, value) -> {
			value.forEach(follwee -> allUsers.compute(follwee, (k, v) -> v == null ? v = 1 : v + 1));
		});
		allUsers.entrySet().stream().filter(e -> e.getValue() >= toBeInflue).forEach(person -> infl.add(person.getKey()));
		return infl;
	}
	
}																			 
																				 























