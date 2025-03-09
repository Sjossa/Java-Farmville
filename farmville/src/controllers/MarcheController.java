package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.ProduitMarche;
import models.ProduitReserve;
import models.Stock;

public class MarcheController {

    @FXML
    private ListView<ProduitMarche> graineList, animauxList;
    @FXML
    private ListView<String> panier;
    @FXML
    private Label soldeLabel, totalPanierLabel, messageLabel;
    @FXML
    private TextField quantiteGraine, quantiteAnimal;

    private double solde = 1000;
    private final ObservableList<String> panierProduits = FXCollections.observableArrayList();
    private final ObservableList<ProduitMarche> graines = FXCollections.observableArrayList();
    private final ObservableList<ProduitMarche> animaux = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initialiserProduits();
        configurerListes();
        panier.setItems(panierProduits);
        updateUI();
    }

    private void initialiserProduits() {
        graines.addAll(
                new ProduitMarche("Blé", "GRA001", 10.0),
                new ProduitMarche("Maïs", "GRA002", 12.0)
        );

        animaux.addAll(
                new ProduitMarche("Vache", "ANI001", 100.0),
                new ProduitMarche("Poulet", "ANI002", 40.0)
        );
    }

    private void configurerListes() {
        graineList.setItems(graines);
        animauxList.setItems(animaux);

        graineList.setCellFactory(param -> new ProduitCell());
        animauxList.setCellFactory(param -> new ProduitCell());
    }

    @FXML
    public void ajouterGrainePanier() {
        ajouterProduitAuPanier(graineList, quantiteGraine);
    }

    @FXML
    public void ajouterAnimalPanier() {
        ajouterProduitAuPanier(animauxList, quantiteAnimal);
    }

    private void ajouterProduitAuPanier(ListView<ProduitMarche> listView, TextField quantiteField) {
        ProduitMarche produit = listView.getSelectionModel().getSelectedItem();
        if (produit == null) return;

        try {
            int quantite = Integer.parseInt(quantiteField.getText().trim());
            if (quantite <= 0) {
                messageLabel.setText("Veuillez entrer une quantité positive !");
                return;
            }
            panierProduits.add(produit.getNom() + " x" + quantite);
            quantiteField.clear();
            updateUI();
        } catch (NumberFormatException e) {
            messageLabel.setText("Veuillez entrer une quantité valide !");
        }
    }

    @FXML
    public void acheterPanier() {
        double totalPanier = panierProduits.stream()
                .mapToDouble(this::calculerPrixTotalProduit)
                .sum();

        if (solde < totalPanier) {
            messageLabel.setText("Solde insuffisant !");
            return;
        }

        solde -= totalPanier;

        // Ajouter les produits du panier au stock
        ajouterProduitsAuStock();

        panierProduits.clear();
        updateUI();
        messageLabel.setText("Achat effectué et ajouté à la réserve !");
    }

    private void ajouterProduitsAuStock() {
        for (String item : panierProduits) {
            String[] parts = item.split(" x");
            String produitNom = parts[0];
            int quantite = Integer.parseInt(parts[1]);

            // Trouver l'ID et le prix du produit
            String id = trouverIdProduit(produitNom);
            double prix = trouverPrixProduit(produitNom);

            // Créer un ProduitReserve et l'ajouter au Stock
            ProduitReserve produitReserve = new ProduitReserve(produitNom, id, quantite);
            Stock.getInstance().ajouterProduit(produitReserve);
        }
    }

    private String trouverIdProduit(String produitNom) {
        return graines.stream()
                .filter(p -> p.getNom().equals(produitNom))
                .map(ProduitMarche::getId)
                .findFirst()
                .orElse(animaux.stream()
                        .filter(p -> p.getNom().equals(produitNom))
                        .map(ProduitMarche::getId)
                        .findFirst()
                        .orElse(null));
    }

    private double trouverPrixProduit(String produitNom) {
        return graines.stream()
                .filter(p -> p.getNom().equals(produitNom))
                .mapToDouble(ProduitMarche::getPrix)
                .findFirst()
                .orElse(animaux.stream()
                        .filter(p -> p.getNom().equals(produitNom))
                        .mapToDouble(ProduitMarche::getPrix)
                        .findFirst()
                        .orElse(0.0));
    }

    private double calculerPrixTotalProduit(String panierItem) {
        String[] parts = panierItem.split(" x");
        String produitNom = parts[0];
        int quantite = Integer.parseInt(parts[1]);

        return trouverPrixProduit(produitNom) * quantite;
    }

    private void updateUI() {
        soldeLabel.setText("Solde : " + solde + " pièces");
        double totalPanier = panierProduits.stream().mapToDouble(this::calculerPrixTotalProduit).sum();
        totalPanierLabel.setText("Total panier : " + totalPanier + " pièces");
    }

    @FXML
    public void viderPanier() {
        panierProduits.clear();
        updateUI();
    }

    private static class ProduitCell extends ListCell<ProduitMarche> {
        @Override
        protected void updateItem(ProduitMarche item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.getNom() + " - " + item.getPrix() + " pièces");
        }
    }
}
