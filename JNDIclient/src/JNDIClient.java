import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class JNDIClient {

    public static void main(String[] args) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
            env.put(Context.PROVIDER_URL, "file:///C:/JNDI");

            Context context = new InitialContext(env);

            // Modification ici : suppression de "rmi:"
            Bibliotheque bibliotheque = (Bibliotheque) context.lookup("bibliotheque");

            Livre livre1 = new Livre("Le Petit Prince", "Antoine de Saint-Exupéry");
            bibliotheque.ajouterLivre(livre1);

            Livre livre2 = new Livre("1984", "George Orwell");
            bibliotheque.ajouterLivre(livre2);

            System.out.println("Livres dans la bibliothèque:");
            bibliotheque.listerTousLesLivres();

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}