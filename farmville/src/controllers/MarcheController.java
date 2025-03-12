package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;

import java.util.stream.Stream;

public class MarcheController {

    @FXML
    private ListView<ProduitMarche> graineList, animauxList;
    @FXML
    private ListView<ProduitReserve> stockList;
    @FXML
    private ListView<String> panier;
    @FXML
    private Label soldeLabel, totalPanierLabel, messageLabel;
    @FXML
    private TextField quantiteGraine, quantiteAnimal, quantiteFieldVente;

    private final ObservableList<String> panierProduits = FXCollections.observableArrayList();
    private final ObservableList<ProduitMarche> graines = FXCollections.observableArrayList();
    private final ObservableList<ProduitMarche> animaux = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        initialiserProduits();
        configurerListView(graineList, graines);
        configurerListView(animauxList, animaux);
        stockList.setItems(Stock.getInstance().getProduitsReserve());
        panier.setItems(panierProduits);
        mettreAJourUI();
    }

    private void initialiserProduits() {
        graines.addAll(new ProduitMarche("Blé", "GRA001", 10.0),
                new ProduitMarche("Carotte", "GRA002", 12.0));

        animaux.addAll(new ProduitMarche("Vache", "ANI001", 100.0),
                new ProduitMarche("Poule", "ANI002", 40.0));
    }

    private void configurerListView(ListView<ProduitMarche> listView, ObservableList<ProduitMarche> produits) {
        listView.setItems(produits);
        listView.setCellFactory(param -> new ProduitCell());
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
        if (produit == null) {
            afficherMessageErreur("Veuillez sélectionner un produit.");
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteField.getText().trim());
            if (quantite <= 0) {
                afficherMessageErreur("Veuillez entrer une quantité positive !");
                return;
            }

            panierProduits.add(produit.getNom() + " x" + quantite);
            quantiteField.clear();
            mettreAJourUI();
        } catch (NumberFormatException e) {
            afficherMessageErreur("Veuillez entrer une quantité valide !");
        }
    }

    @FXML
    public void acheterPanier() {
        double totalPanier = panierProduits.stream()
                .mapToDouble(this::calculerPrixTotalProduit)
                .sum();

        if (!Banque.getInstance().retirerArgent(totalPanier)) {
            afficherMessageErreur("Solde insuffisant !");
            return;
        }

        ajouterProduitsAuStock();
        panierProduits.clear();
        mettreAJourUI();
        afficherMessageSuccès("Achat effectué et ajouté à la réserve !");
    }

    private void ajouterProduitsAuStock() {
        for (String item : panierProduits) {
            String[] parts = item.split(" x");
            String produitNom = parts[0];
            int quantite = Integer.parseInt(parts[1]);

            ProduitMarche produit = trouverProduit(produitNom);
            if (produit != null) {
                Stock.getInstance().ajouterProduit(new ProduitReserve(produitNom, produit.getId(), quantite));
            }
        }
    }

    private ProduitMarche trouverProduit(String produitNom) {
        return Stream.concat(graines.stream(), animaux.stream())
                .filter(p -> p.getNom().equals(produitNom))
                .findFirst()
                .orElse(null);
    }

    private double calculerPrixTotalProduit(String panierItem) {
        String[] parts = panierItem.split(" x");
        String produitNom = parts[0];
        int quantite = Integer.parseInt(parts[1]);
        return trouverProduit(produitNom).getPrix() * quantite;
    }

    private void mettreAJourUI() {
        soldeLabel.setText("Solde : " + Banque.getInstance().getSolde() + " pièces");
        totalPanierLabel.setText("Total panier : " + panierProduits.stream().mapToDouble(this::calculerPrixTotalProduit).sum() + " pièces");
    }

    @FXML
    public void viderPanier() {
        panierProduits.clear();
        mettreAJourUI();
    }

    @FXML
    public void vendreProduit() {
        ProduitReserve produitReserve = stockList.getSelectionModel().getSelectedItem();
        if (produitReserve == null) {
            afficherMessageErreur("Veuillez sélectionner un produit dans la réserve.");
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteFieldVente.getText().trim());
            if (quantite <= 0 || quantite > produitReserve.getQuantite()) {
                afficherMessageErreur("Quantité invalide.");
                return;
            }

            double prixDeVente = produitReserve.getPrix() * quantite;
            Banque.getInstance().ajouterArgent(prixDeVente);
            produitReserve.setQuantite(produitReserve.getQuantite() - quantite);

            if (produitReserve.getQuantite() == 0) {
                Stock.getInstance().getProduitsReserve().remove(produitReserve);
            }

            mettreAJourUI();
            afficherMessageSuccès("Vente effectuée avec succès !");
        } catch (NumberFormatException e) {
            afficherMessageErreur("Veuillez entrer une quantité valide !");
        }
    }

    private void afficherMessageErreur(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
    }

    private void afficherMessageSuccès(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
    }

    private static class ProduitCell extends ListCell<ProduitMarche> {
        @Override
        protected void updateItem(ProduitMarche item, boolean empty) {
            super.updateItem(item, empty);
            setText(empty || item == null ? null : item.getNom() + " - " + item.getPrix() + " pièces");
        }
    }
}
