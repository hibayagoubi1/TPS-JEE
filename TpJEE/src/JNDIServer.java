import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

public class JNDIServer {

    public static void main(String[] args) {
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
            env.put(Context.PROVIDER_URL, "file:///C:/JNDI");

            Context context = new InitialContext(env);

            // Option 1: Utiliser rebind() au lieu de bind()
            Bibliotheque bibliotheque = new Bibliotheque();
            context.rebind("bibliotheque", bibliotheque);

            System.out.println("Bibliotheque enregistrée/mise à jour dans JNDI avec succès!");


            context.close();

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}