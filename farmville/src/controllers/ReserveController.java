package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Reserve;

import java.util.Map;

public class ReserveController {

    @FXML
    private ListView<String> stockListView;  // Liste pour afficher le stock
    @FXML
    private Label reserveLabel;  // Label pour afficher les informations du stock
    @FXML
    private Button ajouterButton;  // Bouton pour ajouter une ressource
    @FXML
    private Button retirerButton;  // Bouton pour retirer une ressource
    @FXML
    private TextField ressourceField;  // Champ de texte pour entrer le nom de la ressource
    @FXML
    private TextField quantiteField;  // Champ de texte pour entrer la quantité

    private Reserve reserve;

    public ReserveController() {
        reserve = new Reserve();  // Initialisation de la réserve
    }

    @FXML
    public void initialize() {
        afficherStock();
    }

    // Méthode pour afficher le stock actuel dans la ListView
    private void afficherStock() {
        stockListView.getItems().clear();
        for (Map.Entry<String, Integer> entry : reserve.getStock().entrySet()) {
            stockListView.getItems().add(entry.getKey() + " : " + entry.getValue());
        }
    }

    // Méthode pour ajouter une ressource à la réserve
    @FXML
    private void ajouterStock() {
        String ressource = ressourceField.getText();
        int quantite = Integer.parseInt(quantiteField.getText());

        if (ressource != null && !ressource.isEmpty() && quantite > 0) {
            reserve.ajouterStock(ressource, quantite);
            afficherStock();  // Mettre à jour la vue du stock
            ressourceField.clear();
            quantiteField.clear();
        }
    }

    // Méthode pour retirer une ressource du stock
    @FXML
    private void retirerStock() {
        String ressource = ressourceField.getText();
        int quantite = Integer.parseInt(quantiteField.getText());

        if (ressource != null && !ressource.isEmpty() && quantite > 0) {
            boolean success = reserve.retirerStock(ressource, quantite);
            if (success) {
                afficherStock();  // Mettre à jour la vue du stock
            } else {
                reserveLabel.setText("Pas assez de ressources pour retirer.");
            }
            ressourceField.clear();
            quantiteField.clear();
        }
    }
}
