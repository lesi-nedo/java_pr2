

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import SocialNetwork.SocialNetwork;
import errorNet.ContenutoOffensivoException;
import errorNet.NonPuoiSeguireException;
import errorPost.LimiteSuperatoException;
import errorPost.NoPermissionException;
import errorPost.NoPostException;
import errorPost.NonInseritoException;
import errorPost.UserNameSbagliatoException;
import post.*;


public class Main {
	
	public static void main(String[] args) {
		SocialNetwork social = new SocialNetwork();
		try {     
    	//inserisce dal file json user e status nella SocialNetwork
        	Gson gson = new GsonBuilder().setPrettyPrinting().create();
        	List<testClass> jsonList = gson.fromJson(new FileReader("InputForTest.json"), new TypeToken<ArrayList<testClass>>(){}.getType());
        	jsonList.forEach(list -> {
        		try {
        		// Qua viene eseguito l'eccezione per il superamento dei numeri di caratteri.
        			social.insert(list.getUser(), list.getPost());
        		} catch (ContenutoOffensivoException e) {
        			System.out.println(e.getMessage());
        		}
        	});
    	} catch (FileNotFoundException  e) {
        	e.printStackTrace(); 
    	} catch (NullPointerException e) {
    		System.out.println(e.getMessage());
    	} 
		//Creiamo una lista per far passare al metodo guessFollowers
		List<Post> list = new ArrayList<Post>();
		try {
			AtomicInteger id = new AtomicInteger(0);
			list.add(new Post(id, "Napoleon", "Che cosa Hanno in comune un televisore e una formica? Le antenne #Wellesley "));
			id.getAndIncrement();
			list.add(new Post(id, "Muhammad", "Qual e' la citta preferita dai ragni? Mosca! #Jesus"));
			id.getAndIncrement();
			list.add(new Post(id, "Lincoln", "Che cose'e' una zebra?Un cavallo evaso dal carcere. #Washington. #3dfs o #_fds <-- non sono menzionamenti."));
			id.getAndIncrement();
			list.add(new Post(id, "Shakespeare", "Qual e' la pianta piu puzzolente?Quella dei piedi!"));
		} catch (IllegalArgumentException | NullPointerException | NoPostException | LimiteSuperatoException | UserNameSbagliatoException e ) {
			System.out.println(e.getMessage());
		}
		//Deriviamo dai posts la social.  Chiama sulla coppia del social al suo interno. Unico modo per inserire nella social (Map<String, Set<String>) interna e' tramite il metodo insert o seguire dei post.
		Map<String, Set<String>> test = social.guessFollowers(list);
		//Controlliamo se guessFollowers ha fatto il suo lavoro
		System.out.println("------> " + test.containsKey("Napoleon"));
		test.forEach((k,v) -> System.out.println(k + " : " + v));
		//Vediamo chi sono gli influencers. Non ci sono gli user creati sopra poiche il metodo guessFollowers chiama sulla coppia del social.
		System.out.println("INFLUENCERS: " + social.influencers());
		//Restituisce tutte le persone menzionate nella social.
		System.out.println("MENZIONATE:   " + social.getMentionedUsers());
		//restituisce tutte le persone menzionate da uno specifico utente della social network
		System.out.println("MENZIONATE UTENTE SOCIAL:   " + social.getMentionedUsers("actcomms"));
		//restituisce l'insieme degli utenti menzionati nella lista.
		System.out.println("MENZIONATI NELLA LISTA:    " + social.getMentionedUsers(list));
		//Restituisce la lista dei post che user ha scritto
		System.out.println("HA SCRITTO:   " + social.writtenBy("yThomasm"));
		//Proviamo a inserire un username della forma scorretta.
		try {
			//restituisce menzionati da un utente specifico
			System.out.println("MENZIONATI UTENTE LISTA:    " + social.getMentionedUsers(list, "Napoleon"));
			//Restituisce la lista dei post effettuati dall'utente nella lista.
			System.out.println("HA SCRITTO LA PERSONA NELLA LISTA:   " + social.writtenBy(list, "yThomasm"));
			//Restituisce la lista dei post effettuati dall'utente nella lista.
			System.out.println("HA SCRITTO LA PERSONA NELLA LISTA:   " + social.writtenBy(list, "Lincoln"));
			social.insert("3NonPuoComminciareConUnNumero", "Non verra' inserito nel social");
		} catch(ContenutoOffensivoException | UserNameSbagliatoException e) {
			System.out.println(e.getMessage());
		}
		/*                                                                           POST CON CONTENUTO OFFENSIVO                                                */
		try {
			social.insert("ContenutoOffensivo", "Buona notte, cara la mia ragazza, cachi nel letto finché non si scassa, stia chiotta chiotta, si stiri il culo fino alla bocca; io vo al paese di cuccagna, per fare anche io un poco di nanna.");
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.insert("ContenutoOffensivo", " Ti prometto che la prossima volta non manderò tutto a puttane, mamma.\n" + 
					"- Che linguaggio, tesoro!\n" + 
					"- Scusa. La prossima volta non manderò tutto a puttane, mammina.");
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.insert("ContenutoOffensivo", "Oggi voglio dire una parola che alla radio non si dice mai: Cazzo!");
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.insert("ContenutoOffensivo", "- Venticello: Mannaggia la mignotta...\n" + 
					"- Madre di Venticello: Che me hai chiamato?");
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.insert("ContenutoOffensivo", "Lo stronzo che scrive parolacce\n" + 
					"deve andare a fare in culo.");
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.insert("ContenutoOffensivo", "pu$$y");
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.insert("ContenutoOffensivo", "Se in una frase c'è la parola #43culo(#, il pubblico, foss'anche una frase sublime, sentirà solo questa parola.");
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		
		/*                                                                        FINE                                                     */
		
		//Come primo carattere non puo essere un simbolo.
		try {
			social.insert(")random", "Non verra' inserito nel social");
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		
		//Inseriamo un post per por seguirlo.
		try {
			social.insert("Questo_UserName34234ECorretto", "La vita inizia dove finisce la paura");
			System.out.println("OCCORRENZA E':    " + social.getNumOcc("Questo_UserName34234ECorretto"));
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		//seguiamo il post. Trova il primo post che e' uguale al dato testo e si ferma.
		try {
			System.out.println("RISULTATO FOLLOW WITH TEXT  :  " +social.followWithText("La vita inizia dove finisce la paura", "Okean"));
		} catch (NonPuoiSeguireException | UserNameSbagliatoException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		//Seguiamo il post con il suo ID
		try {
			System.out.println("RISULTATO FOLLOW WITH ID:  " +social.followWithId(34, "Monatik"));	
		} catch (NonPuoiSeguireException | UserNameSbagliatoException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		//proviamo a seguire di nuovo lo stesso post.
		try {
			System.out.println("RISULTATO:  " + social.followWithId(34, "Monatik"));	
		} catch (NonPuoiSeguireException | UserNameSbagliatoException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		//proviamo a seguire un post con un Post
		try {
			System.out.println("RISULTATO: FOLLOW WITH POST " +social.followWithPost(social.getById(78), "Monatik"));	
			System.out.println("RISULTATO:  " +social.seguiti("Monatik")); //il metodo va in cerca dei post che segue monatik
		} catch (NonPuoiSeguireException | UserNameSbagliatoException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		
		ArrayList<String> toCheck = new ArrayList<String>();
		toCheck.add("hi");
		toCheck.add("sorry");
		toCheck.add("come back");
		toCheck.add("cool");
		toCheck.add("vita");
		//chiamiamo containing con la lista di parole.
		System.out.println("RISULTATO CONTAINING" + social.containing(toCheck));
		//restituisce il numero dei post pubblicati
		System.out.println( social.getNumOcc("Monatik"));
		//inseriamo un nuovo post 
		try {
			social.insert("Monatik", "La gente non e' povera per come vive, e' povera per come pensa");
			social.insert("Monatik", "La vita e' questa. Niente e' facile e nulla e' impossibile. #Morgenstern");
			social.insert("Monatik", "#Gutenberg #Bonaparte #Luther #Max #Einstein");
		} catch(ContenutoOffensivoException e) {
			System.out.println(e.getMessage());
		}
		//risulta corretto.
		System.out.println(social.getNumOcc("Monatik"));
	
		try {
			System.out.println("RISULTATO:  SEGUITI  " + social.seguiti("Monatik")); //il metodo va in cera dei post che segue monatik
			System.out.println(" RISULTATO: WHO FOLLOWS     " + social.whoFollows("Monatik")); //restituisce le persone che segue
		} catch (UserNameSbagliatoException | NullPointerException e) {
			System.out.println(e.getMessage());
		} 
		
		//eliminiamo un username
		try {
			social.deleteUsername("Monatik");
		} catch(NoPermissionException e) {
			System.out.println(e.getMessage());
		}
		System.out.println(" RISULTATO: GET FREQ.     " + social.getNumOcc("Monatik"));
		try {
			System.out.println(" RISULTATO:   SUGUITI   " +social.seguiti("Monatik")); //il metodo va in cera dei post che segue monatik
			System.out.println(" RISULTATO:  WHO FOLLOWS    " + social.whoFollows("Monatik")); //restituisce le persone che segue.
		} catch (UserNameSbagliatoException | NullPointerException e) {
			System.out.println(e.getMessage());// viene chiamato.
		} 
		//proviamo a scancellare admin per rompere IR
		try {
			social.deleteUsername("Admin"); //proviamo a cancellare admin, questo utente serve per far rispettare IR
		} catch(NoPermissionException e) {
			System.out.println(e.getMessage());
		}
		//restituisce tutte le persone menzionate con il numero di volete 
		System.out.println("TOTAL:    " + social.numMenzionOcc());
		//restituisce tutti i post seguiti
		System.out.println("SEGUITI:    " + social.tuttiSeguiaci());
		
		
		
		
		/*                                                                  CHIAMATA AI METODI CON NULL E VUOTI OGGETTI                          */
		
		try {
			social.guessFollowers(null);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.guessFollowers(new ArrayList<Post>());
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.deleteUsername(null);
		} catch (NullPointerException |  NoPermissionException e) {
			System.out.println(e.getMessage());
		} 
		try {
			social.containing(null);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.followWithId(1, null);
		} catch (NullPointerException | NonPuoiSeguireException | UserNameSbagliatoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.writtenBy(null);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.getById(new ArrayList<Post>(), 24);
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.followWithPost(null, null);
		} catch (NullPointerException | UserNameSbagliatoException |  NonPuoiSeguireException e) {
			System.out.println(e.getMessage());
		}
		try {
			//in questa situazione e' consentito una lista vuota.
			social.insert(new ArrayList<Post>(), "sdaf", "asfa");
		} catch (NullPointerException  | NonInseritoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.writtenBy(new ArrayList<Post>(), "asfasf");
		} catch (NullPointerException | UserNameSbagliatoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.followWithText(null, "Napoleon");
		} catch (NullPointerException | UserNameSbagliatoException | NonPuoiSeguireException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.getMentionedUsers(new ArrayList<Post>(), "Asdf");
		} catch (NullPointerException | UserNameSbagliatoException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.getMentionedUsers(new ArrayList<Post>());
		} catch (NullPointerException e) {
			System.out.println(e.getMessage());
		}
		try {
			social.getMentionedUsers(new ArrayList<Post>(), "sdsdf");
		} catch (NullPointerException | UserNameSbagliatoException e) {
			System.out.println(e.getMessage());
		}
	
	}
}
























