import java.io.*;
import java.sql.*;
import javax.sql.DataSource;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class LivreManager extends HttpServlet {
    private DataSource dataSource;

    public void init() throws ServletException {
        try {
            InitialContext initContext = new InitialContext();
            dataSource = (DataSource) initContext.lookup("java:comp/env/jdbc/livresDB");
        } catch (NamingException e) {
            throw new ServletException("Impossible d'initialiser la source de données", e);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("ajouter".equals(action)) {
            ajouterLivre(request, response);
        } else if ("supprimer".equals(action)) {
            supprimerLivre(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if ("afficher".equals(action)) {
            afficherLivres(request, response);
        }
    }

    private void ajouterLivre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");
        String titre = request.getParameter("titre");
        String auteur = request.getParameter("auteur");
        double prix = Double.parseDouble(request.getParameter("prix"));
        int anneePublication = Integer.parseInt(request.getParameter("annee_publication"));

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO livre (isbn, titre, auteur, prix, annee_publication) VALUES (?, ?, ?, ?, ?)")) {
            
            pstmt.setString(1, isbn);
            pstmt.setString(2, titre);
            pstmt.setString(3, auteur);
            pstmt.setDouble(4, prix);
            pstmt.setInt(5, anneePublication);
            
            pstmt.executeUpdate();
            
            response.sendRedirect("LivreManager?action=afficher");
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de l'ajout du livre", e);
        }
    }

    private void supprimerLivre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM livre WHERE isbn = ?")) {
            
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
            
            response.sendRedirect("LivreManager?action=afficher");
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de la suppression du livre", e);
        }
    }

    private void afficherLivres(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM livre")) {
            
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            
            out.println("<html><body>");
            out.println("<h2>Liste des livres</h2>");
            out.println("<table border='1'>");
            out.println("<tr><th>ISBN</th><th>Titre</th><th>Auteur</th><th>Prix</th><th>Année de publication</th><th>Action</th></tr>");
            
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("isbn") + "</td>");
                out.println("<td>" + rs.getString("titre") + "</td>");
                out.println("<td>" + rs.getString("auteur") + "</td>");
                out.println("<td>" + rs.getDouble("prix") + "</td>");
                out.println("<td>" + rs.getInt("annee_publication") + "</td>");
                out.println("<td><form method='post' action='LivreManager'>");
                out.println("<input type='hidden' name='action' value='supprimer'>");
                out.println("<input type='hidden' name='isbn' value='" + rs.getString("isbn") + "'>");
                out.println("<input type='submit' value='Supprimer'>");
                out.println("</form></td>");
                out.println("</tr>");
            }
            
            out.println("</table>");
            out.println("<h3>Ajouter un nouveau livre</h3>");
            out.println("<form method='post' action='LivreManager'>");
            out.println("<input type='hidden' name='action' value='ajouter'>");
            out.println("ISBN: <input type='text' name='isbn'><br>");
            out.println("Titre: <input type='text' name='titre'><br>");
            out.println("Auteur: <input type='text' name='auteur'><br>");
            out.println("Prix: <input type='number' name='prix' step='0.01'><br>");
            out.println("Année de publication: <input type='number' name='annee_publication'><br>");
            out.println("<input type='submit' value='Ajouter'>");
            out.println("</form>");
            out.println("</body></html>");
        } catch (SQLException e) {
            throw new ServletException("Erreur lors de l'affichage des livres", e);
        }
    }
}