package post;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import errorPost.UserNameSbagliatoException;

public final class staticMethodsForPost {
	//Overview: L'insieme dei metodi statici per lavorare sulla lista dei post
	//AF: f(List<Post>) || f(List<Post>, username) -> instance of Object
	//IR: ps != NULL && username != NULL && ps[i] != NULL
	
	
	
	//Requires: ps && username != NULL
	//Throws: NullPointerException se ps  == NULL.
	//Effects: restituisce la lista con tutti i post effettuati dalla persona cercata, NULL altrimenti.
	public static <I extends List<Post>> List<Post> writtenBy(I ps, String username) throws NullPointerException{
		if(ps == null || ps.isEmpty()) throw new NullPointerException("Inserire una collezione di posts non nulli.");
		if(ps.contains(null)) throw new NullPointerException("Attenzione un elemento nella lista e' vuoto.");
		ArrayList<Post> allPosts = new ArrayList<Post>();
	    final AtomicInteger i = new AtomicInteger(0);
		ps.forEach(posts -> {if(posts.getUser().equals(username)) allPosts.add(i.getAndIncrement(), posts);});
		return allPosts;
	}
	//Requires: ps != NULL && ps[i] != NULL
	//Throws: NullPointerEsception
	//Effects: restituisce tutte le persone menzionate dall'username, NULL altrimenti.
	public static <I extends List<Post>> Set<String> getMentionedUsers(I ps) throws NullPointerException {
		if(ps == null || ps.isEmpty()) throw new NullPointerException("Inserire un collezione corretta.");
		if(ps.contains(null)) throw new NullPointerException("Attenzione un elemento nella lista e' vuoto.");
		Set<String> mentionedPeople = new HashSet<String>();
		ps.forEach(postTo -> {
			Map<String, Integer> temp = postTo.getTagged();
			if(temp != null && !temp.isEmpty()) temp.keySet().forEach(key -> mentionedPeople.add(key));
		
		});
		return mentionedPeople;
	}
	
	//Requires: userName != NULL && ps[i] != NULL
	//Throws: NullPointerException se userName == NULL, UserNameSbagliatoException
	//Effects: restituisce tutte le persone menzionate dall'username, NULL altrimenti
	public static <I extends List<Post>> Set<String> getMentionedUsers(I ps, String username) throws NullPointerException, UserNameSbagliatoException{
		if(ps == null || ps.isEmpty() || username == null) throw new NullPointerException("La collezione dei post o username non puo' essere NULL");
		if(ps.contains(null)) throw new NullPointerException("Attenzione un elemento nella lista e' vuoto.");
		for(Post posts: ps) {
			if(posts.getUser().equals(username)) {
				Set<String> hash = posts.getTagged().keySet();
				return hash;
			}
		}
		return null;
	}
	
	//Requires: words != NULL && ps[i] != NULL
	//Throws: NullPonterException
	//Effects: restituisce una lista dei post, i quali  contengono almeno una parola delle parole presenti nella lista di words, null altrimenti.
	public static List<Post> containing(List<Post> ps, List<String> words) throws NullPointerException{
		if(ps == null || ps.isEmpty()|| words == null || words.isEmpty()) throw new NullPointerException("Gli argomenti non possono essere nulli.");
		if(ps.contains(null) || words.contains(null)) throw new NullPointerException("Attenzione un elemento nella lista dei post o delle parole e' vuoto.");
		List<Post> allPosts = new ArrayList<Post>();
		TreeMap<String, HashSet<Integer>> tree = new TreeMap<String, HashSet<Integer>>();
		ps.forEach(post -> {
			String[] str = post.getPost().split("\\s+");
			//Ogni parole viene inserita nella TreeMap, e come value ha una HashSet con tutti l'id dei post con parole uguali. Poiche' ogni post ha un Id diverso allora avremo tutti i post.
			//La complessita' e' Ogrande(|posts|)*70 (caso pessimo) 
			for(String word : str) {
				tree.compute(word, (key, value) -> {
					if(value == null) {
					    value = new HashSet<Integer>();
					    value.add(post.getId());
					    return value;
					} else {
						value.add(post.getId());
						return value;
					}
				});
			}
		});
		//Mette nella Hashtable tutti i post con key = id (che e' diverso per tutti) e value il post.
		Hashtable<Integer, Post> hashedById = new Hashtable<Integer, Post>();
		ps.forEach(post -> hashedById.compute(post.getId(), (key, value) -> post));
		ArrayList<Integer> arrId = new ArrayList<Integer>(); //Contiene tutti gli id dei post che hanno almeno una parola delle parole date.
		//Ogni parola del argomento words viene presa e passata al treeMap per vedere se appartiene all'albero. Se si la treeMap ha come value tutti gli Id dei post che hanno la parola.
		words.forEach(word -> tree.computeIfPresent(word, (key, value) -> {value.forEach(id -> arrId.add(id));; return value;}));
		//Per ogni id si va a recupera il post nella hashedById
		arrId.forEach(id -> allPosts.add(hashedById.get(id)));
		return allPosts;
	}
	//Requires: ps != NULL && ps[i] != NULL
	//Throws: NullPointerException
	//Effects: restituisce tutte le persone menzionate con il numero di volte in una Hashtable dove k = username e value = numero di citazioni.
	public static <I extends List<Post>> Map<String, Integer> numMenzionOcc (I ps){
		if(ps == null || ps.isEmpty()) throw new NullPointerException("La lista non puo essere vuota");
		if(ps.contains(null)) throw new NullPointerException("Attenzione un elemento nella lista e' vuoto.");
		Map<String, Integer> total = new Hashtable<String, Integer>();
		ps.forEach(post -> {
		Map<String, Integer> ment = null;
			if((ment = post.getTagged()) != null) {
				suporNumMenzion(ment, total); //ment e' un componente del post cioe la Map che contiene tutte persone menzionate nel post
			}                                 //total sarebbe unione di tutti post.PersoneMenzionate.
		});
		return total;
	}
	//Requires Map != NULL, che e' assicurato dalla condizione del metodo chiamante
	//Effects: un metodo di appoggio per numMenzionOcc per non avere il codice troppo annidato.
	private static void suporNumMenzion (Map<String, Integer> ment, Map<String, Integer> toInsert) {
		ment.forEach((k, v) -> {
			if(toInsert.putIfAbsent(k, v) != null) toInsert.computeIfPresent(k, (key, value) -> value + 1);
		});
	}
	//Effects: restituisce true se l'username non e' corretto
	public static boolean checkUserName (String regex, String username) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(username);
		return matcher.find();
	}
}































