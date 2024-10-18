import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;

public class Bibliotheque implements Serializable, Referenceable {
    private List<Livre> livres;

    public Bibliotheque() {
        this.livres = new ArrayList<Livre>();
    }

    public void ajouterLivre(Livre livre) {
        livres.add(livre);
        System.out.println("Livre ajouté : " + livre);
    }

    public Livre rechercherLivre(String titre) {
        for (Livre livre : livres) {
            if (livre.getTitre().equalsIgnoreCase(titre)) {
                return livre;
            }
        }    System.out.println("Livre non trouvé pour le titre : " + titre);
        return null;
    }

    public List<Livre> listerTousLesLivres() {
        if (livres.isEmpty()) {
            System.out.println("Aucun livre dans la bibliothèque.");
        } else {
            for (Livre livre : livres) {
                System.out.println(livre.getTitre());
                System.out.println(livre.getAuteur());

            }
        }
        return livres;
    }
    @Override
    public Reference getReference() throws NamingException {
        return new Reference(
                getClass().getName(),
                BibliothequeFactory.class.getName(),
                null
        );
    }
}

