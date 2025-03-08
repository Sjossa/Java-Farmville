package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.ProduitMarche;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
        graines.addAll(
                new ProduitMarche("Blé", "GRA001", 10.0),
                new ProduitMarche("Maïs", "GRA002", 12.0)
        );

        animaux.addAll(
                new ProduitMarche("Vache", "ANI001", 100.0),
                new ProduitMarche("Poulet", "ANI002", 40.0)
        );

        // Personnalisation de l'affichage des listes
        graineList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ProduitMarche item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom() + " - " + item.getPrix() + " pièces");
            }
        });

        animauxList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(ProduitMarche item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNom() + " - " + item.getPrix() + " pièces");
            }
        });

        graineList.setItems(graines);
        animauxList.setItems(animaux);
        panier.setItems(panierProduits);

        updateUI();
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
        ProduitMarche produitSelectionne = listView.getSelectionModel().getSelectedItem();
        if (produitSelectionne != null) {
            try {
                int quantite = Integer.parseInt(quantiteField.getText().trim());
                if (quantite <= 0) {
                    messageLabel.setText("Veuillez entrer une quantité positive !");
                    return;
                }
                panierProduits.add(produitSelectionne.getNom() + " x" + quantite + "     " + produitSelectionne.getPrix() + "piece");
                quantiteField.clear(); // Effacer le champ après l'ajout
                updateUI();
                messageLabel.setText("");
            } catch (NumberFormatException e) {
                messageLabel.setText("Veuillez entrer une quantité valide !");
            }
        }
    }

    @FXML
    public void acheterPanier() {
        double totalPanier = panierProduits.stream().mapToDouble(this::calculerPrixTotalProduit).sum();

        if (solde >= totalPanier) {
            solde -= totalPanier;
            panierProduits.clear();
            updateUI();
            messageLabel.setText("Achat effectué !");
        } else {
            messageLabel.setText("Solde insuffisant !");
        }
    }

    private double calculerPrixTotalProduit(String panierItem) {
        String[] parts = panierItem.split(" x");
        String produitNom = parts[0];
        int quantite = (parts.length > 1) ? Integer.parseInt(parts[1]) : 1;
        return trouverPrixProduit(produitNom) * quantite;
    }

    private double trouverPrixProduit(String produitNom) {
        return graines.stream()
                .filter(produit -> produit.getNom().equals(produitNom))
                .mapToDouble(ProduitMarche::getPrix)
                .findFirst()
                .orElse(animaux.stream()
                        .filter(produit -> produit.getNom().equals(produitNom))
                        .mapToDouble(ProduitMarche::getPrix)
                        .findFirst()
                        .orElse(0.0));
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
}
