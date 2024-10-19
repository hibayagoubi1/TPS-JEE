<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestion des Livres</title>
</head>
<body>
    <h1>Gestion des Livres</h1>
    
    <h2>Ajouter un nouveau livre</h2>
    <form action="LivreManager" method="post">
        <input type="hidden" name="action" value="ajouter">
        ISBN: <input type="text" name="isbn" required><br>
        Titre: <input type="text" name="titre" required><br>
        Auteur: <input type="text" name="auteur" required><br>
        Prix: <input type="number" name="prix" step="0.01" required><br>
        Année de publication: <input type="number" name="annee_publication" required><br>
        <input type="submit" value="Ajouter le livre">
    </form>
    
    <h2>Afficher tous les livres</h2>
    <form action="LivreManager" method="get">
        <input type="hidden" name="action" value="afficher">
        <input type="submit" value="Afficher les livres">
    </form>
    
    <!-- La liste des livres et le formulaire de suppression seront affichés ici par la servlet -->
    <%= request.getAttribute("livresHtml") != null ? request.getAttribute("livresHtml") : "" %>
</body>
</html>