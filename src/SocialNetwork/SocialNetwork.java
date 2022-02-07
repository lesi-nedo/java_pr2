package SocialNetwork;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import errorNet.ContenutoOffensivoException;
import errorNet.NonPuoiSeguireException;
import errorPost.NoPermissionException;
import errorPost.NonCancellatoException;
import errorPost.NonInseritoException;
import errorPost.UserNameSbagliatoException;
import post.*;

public class SocialNetwork extends buildListOfPost implements SocialNetInterface {
	//Overview: La social network e' la classe che racchiude i post pubblicati, persone seguite da ogni utente, e post seguiti.
	//AF: f(post, seguiti, username) -> Social Network
	//IR: username != NULL && post != NULL && string.match("#([a-z])(\\w+)?(_?)  postsSeguiti[i] != NULL + quella della super classe
	
	Map<String, Set<String>> social; //Ogni key rappresenta un user e value le persone che segue
	buildListOfPost listOfPosts;    //La lista di tutti post, possono esserci post con il text e autore uguali ma non duplicati
	Map<String, List<Seguire>> postsSeguiti; //key rappresenta l'user e Seguire tutti i post seguiti.
	
	//qua salva le parolacce in inglese e italiano
	private TreeMap<String, Integer> badWordsTree = new TreeMap<String, Integer>();
	AtomicInteger numOfWords = new AtomicInteger(0);
	//inizializzazione della classe
	public SocialNetwork(){
		this.listOfPosts = new buildListOfPost();
		this.social = new Hashtable<String, Set<String>>();
		this.postsSeguiti = new Hashtable<String, List<Seguire>>();
		//mette nell'albero tutte le parolacce dal file
		try {
			Stream<String> file = Files.lines(Paths.get("englishBadWords.txt"));
			file.forEach(word -> badWordsTree.put(word, numOfWords.getAndIncrement()));	
			
			Stream<String> file2 = Files.lines(Paths.get("italianBadWords.txt"));
			file2.forEach(word -> badWordsTree.put(word, numOfWords.getAndIncrement()));
			file.close();
			file2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//restituisce una coppia della variabile postsSeguiti
	public Map<String, List<Seguire>> tuttiSeguiaci () {
		return copyMap.copy(postsSeguiti);
	}

	//Requires: username != null
	//Throws: NullPointerException
	//Effects: restituisce dalla social le persone che user segue, alrimenti null
	public Set<String> whoFollows(String username) throws UserNameSbagliatoException, NullPointerException{
		if(username == null) throw new NullPointerException("Il post non puo essere nullo.");
		if(staticMethodsForPost.checkUserName("^[^a-zA-Z]", username) || staticMethodsForPost.checkUserName ("[^a-zA-Z0-9_]+", username)) throw new UserNameSbagliatoException("Username inserito non e' della corretta forma.");
		Set<String> toRet = Collections.unmodifiableSet(social.get(username));
		return toRet;
	}
	//Requires: string != NULL
	//Effects: un metodo privato per controllare i post inserite nella Rete Sociale per segnalare parole offensive.
	private boolean checkPost(String text) {
		Pattern pattern = Pattern.compile("([a-zA-z])([^\\s]+)([a-bA-z])", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()) {
			String toInsert = text.substring(matcher.start(), matcher.end()).toLowerCase();
			if(badWordsTree.containsKey(toInsert)) return true;
		}
		return false;
	}
	
	public void printAllBadWords () {
		badWordsTree.forEach((k,v) -> System.out.println(v + ":   " + k));
	}
	//Effects: Un metodo privato per controllare se un utente sta gia seguendo il pos.
	private boolean giaSegue (Post post, String username) {
		List<Seguire> tuttiSeg = postsSeguiti.get(username);
		if(tuttiSeg != null && !tuttiSeg.isEmpty()) {
			return tuttiSeg.stream().anyMatch(seguiti -> seguiti.whatPost().equals(post));
		}
		return false;
	}
	
	
	//Requires: post != NULL
	//Throws: NullPointerException
	//Modifies: postsSeguiti;
	//Effects: Inserisce il post nella map
	private void insToSeguiti(Post post, String username) {
		if(post == null || post.isEmpty()) throw new NullPointerException("Attenzione il post e' null.");
		postsSeguiti.compute(username, (k, v) -> {
			Seguire foll = new Seguire(post, username);
			if(v == null) {
				v = new ArrayList<Seguire>();
				v.add(foll);
				return v;
			} else {
				v.add(foll);
				return v;
			}
		});
	}
	
	//Requires: chiSegue != NULL && seguito != NULL
	//Modifies: la classe social
	//Effects: inserisce la nuova persona seguita se non e' presente nella social[a].
	private void inserire(String chiSegue, String seguito) throws NullPointerException {
		if(chiSegue == null || seguito == null) throw new NullPointerException();
		social.compute((chiSegue), (key, value) -> {
			if(value == null || value.isEmpty()) {
				value = new HashSet<String>();
				value.add(seguito);
				return value;
			}
			value.add(seguito);
			return value;
		});
	}
		
		
		//restituisce tutti post
		public List<Post> getAllPosts(){
			return CopyOut.copy(getList(listOfPosts));
		}
		
		
		//Request ps != NULL
		//Throws: StaPerResettareException
		//Modifies: social
		//Effects: Si ottiene la rete sociale derivata dalla list.
		public Map<String, Set<String>> guessFollowers(List<Post> ps){
			return staticSocialNetMethods.guessFollowers(copyMap.<Set<String>>copy(social), ps);
			
		}
		//Requires:  ID >= 0 && chiSegue != NULL
		//Throws: IllegalArgumentException, NonPuoiSeguireException
		//Effects: restituisce Una Stringa che dice "Stai seguendo questo post" se il post e' presente, "Stai gia seguendo il pos", altrimenti "Non trovato". 
		@SuppressWarnings("null")
		public String followWithId (int id, String username) throws  IllegalArgumentException, NonPuoiSeguireException, NullPointerException, UserNameSbagliatoException{
			if(id <= 0) throw new IllegalArgumentException("ID del post è un numero maggiore di zero.");
			if(username == null) throw new NullPointerException("Username non puo essere vuot.");
			if(staticMethodsForPost.checkUserName("^[^a-zA-Z]", username) || staticMethodsForPost.checkUserName ("[^a-zA-Z0-9_]+", username)) throw new UserNameSbagliatoException("Username inserito non e' della corretta forma.");
			Post inser = super.getById(getList(listOfPosts), id);
			if(inser.getUser().equals(username)) throw new NonPuoiSeguireException("Non puoi seguire il tuo post.");
			if(inser == null || inser.isEmpty()) return "Non trovato";
			if(giaSegue(inser, username)) return "Stai gia seguendo il post.";
			insToSeguiti(inser, username);
			inserire(username, inser.getUser());
			return "Stai seguendo questo post.";
			
		}
		
		
		//Requires: post != NULL
		//Throws: NullPointerException, NonPuoiSeguire
		//Effects: restituisce come nel caso followWithId
		public String followWithPost(Post postToInser, String username) throws NullPointerException, NonPuoiSeguireException, UserNameSbagliatoException{
			if(postToInser == null) throw new NullPointerException("Il post non puo essere nullo.");
			if(staticMethodsForPost.checkUserName("^[^a-zA-Z]", username) || staticMethodsForPost.checkUserName ("[^a-zA-Z0-9_]+", username)) throw new UserNameSbagliatoException("Username inserito non e' della corretta forma.");
			if(giaSegue(postToInser, username)) return "Stai gia seguendo il post.";
			int yes = 0;
			for(Post post : getList(listOfPosts)) {
				if(post.equals(postToInser)) {
					if(post.getUser().equals(username)) throw new NonPuoiSeguireException("Non puoi seguire il tuo post.");
					insToSeguiti(post, username);
					inserire(username, post.getUser());
					yes = 1;
				}
			}
				
			if(yes == 1) return "Stai seguendo il post.";
			return "Non trovato.";
		}
		//Requires: text != NULL username != NULL e della corretta forma.
		//Throws:
		//Effects: restituisce una stringa e segue il primo post trovato. null altrimenti.
		public String followWithText(String text, String username) throws NullPointerException, NonPuoiSeguireException, UserNameSbagliatoException{
			if(username == null || text == null) throw new NullPointerException("Username non puo essere nullo.");
			if(staticMethodsForPost.checkUserName("^[^a-zA-Z]", username) || staticMethodsForPost.checkUserName ("[^a-zA-Z0-9_]+", username)) throw new UserNameSbagliatoException("Username inserito non e' della corretta forma.");
			for(Post ps: getList(listOfPosts)) {
				if(ps.getPost().equals(text)){
					if(ps.getUser().equals(username)) throw new NonPuoiSeguireException("Non puoi seguire il tuo post.");
					if(giaSegue(ps, username)) return "Stai gia' seguendo il post";
					insToSeguiti(ps, username);
					inserire(username, ps.getUser());
					return "Stai seguendo il post";
				}
			}
			return "Non trovato.";
		}
		
		
		//Requires: username != NULL
		//Throws: NullPointerException, UserNameSbagliatoException
		//Effects: restituisce tutti i post che l'username segue, altrimenti un oggetto vuoto.
		public List<Post> seguiti (String username) throws NullPointerException, UserNameSbagliatoException{
			if(staticMethodsForPost.checkUserName("^[^a-zA-Z]", username) || staticMethodsForPost.checkUserName ("[^a-zA-Z0-9_]+", username)) throw new UserNameSbagliatoException("Username inserito non e' della corretta forma.");
			if(username == null) throw new NullPointerException("Username non puo essere nullo.");
			List<Post> toRet = new ArrayList<Post>();
			List<Seguire> seguiti = postsSeguiti.get(username);
			if(seguiti != null && !seguiti.isEmpty()) {
				seguiti.forEach(pos -> toRet.add(pos.whatPost()));
			}
			return CopyOut.copy(toRet);
		}
		
		public  List<String> influencers(){
			return staticSocialNetMethods.influencers(social);
		}
		//Effects: restituisce la lista dei post effettuati dall'username, altrimenti NULL
		public List<Post> writtenBy(String username){
			try {
				return super.writtenBy(getList(listOfPosts), username);
			} catch (UserNameSbagliatoException | NullPointerException e) {
				System.out.println(e.getMessage());
			}
			return null;
		}
		@Override
		//Effects: Restituisce l'insieme degli utenti menzionati nei posts, NULL altrimenti
		public Set<String> getMentionedUsers() {
			try {
				return super.getMentionedUsers(getList(listOfPosts));
			} catch (NullPointerException e) {
				System.out.println(e.getMessage());
			}
			return null;
		}
		
		
		//Effects: restituisce tutte le persone menzionate dall'username;
		public Set<String> getMentionedUsers(String username){
			try {
				return super.getMentionedUsers(getList(listOfPosts), username);
			} catch (UserNameSbagliatoException | NullPointerException e) {
				System.out.println(e.getMessage());
			}
			return null;
		}

		//restituisce il post trovato con id
		public Post getById (int id){
			try {
				return super.getById(getList(listOfPosts), id);
			} catch ( IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
			return null;
		}
		//Modifies: listOfPosts
		//Effects: inserisce nella lista della social network
		
		public void insert (String username, String post) throws ContenutoOffensivoException {
			if(checkPost(post)) throw new ContenutoOffensivoException("Il contenuto del post e' offensivo");
			Post toExam = null;
			try {
				toExam = super.insert(getList(listOfPosts), username, post);
			} catch (NonInseritoException e) {
				System.out.println(e.getMessage());
			}
			if(toExam != null) toExam.getTagged().forEach((k,v) -> guessFoll(username, k, social));
		}
		//Modifies: social
		//Effects: Ogni volta che si inserisce nella lista dei pos si aggiorna con questo metodo il social (Map<String, Set<String>)
		private static void guessFoll(String user, String person, Map<String, Set<String>> social) {
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
		}
		//Modifies: ListOfposts, social, tuttiSeguiti
		//Effects Elimina
		public void deleteUsername (String username) throws NoPermissionException {
			if(username == null) throw new NullPointerException("User name non puo essere vuoto.");
			if(username.equals("Admin")) throw new NoPermissionException("Non e' possibile cancellare l'user Admin");
			try {
				System.out.println(super.deleteUsername(getList(listOfPosts), username));
				social.remove(username);
				social.forEach((key, value) -> value.remove(username));
				postsSeguiti.remove(username);
			} catch (NullPointerException | NonCancellatoException | NoPermissionException | UserNameSbagliatoException e) {
				System.out.println(e.getMessage());
			}
			
		}
		//Requires: username
		//Effects: restituisce il numero di volte username e'presentenella lista dei post.
		public long getNumOcc (String username) {
			try {
				return super.getNumOcc(getList(listOfPosts), username);
			} catch (NullPointerException e) {
				System.out.println(e.getMessage());
			}
			return 0;
		}
		
		//Requires: words != NULL && ps[i] != NULL
		//Throws: NullPonterException
		//Effects: restituisce una lista dei post, i quali  contengono almeno una parola delle parole presenti nella lista di words, null altrimenti.
		public List<Post> containing(List<String> words){
			return CopyOut.copy(staticMethodsForPost.containing(getList(listOfPosts), words));
		}
		//Effects: restituisce una map con tute persone menzionate come key e come value il numero di volte
		public Map<String, Integer> numMenzionOcc (){
			return staticMethodsForPost.numMenzionOcc(getList(listOfPosts));
		}
}






















































