package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.Marche;
import java.util.HashMap;
import java.util.Map;

public class MarcheController {

    @FXML
    private ListView<HBox> graineList;  // Liste des ressources disponibles
    @FXML
    private ListView<HBox> panier;      // Liste des ressources ajoutées au panier
    @FXML
    private Label balanceLabel;         // Label pour afficher le solde total
    @FXML
    private Label stockLabel;           // Label pour afficher le stock sélectionné
    @FXML
    private Label totalPanierLabel;     // Label pour afficher le total du panier

    private Marche marche;
    private Map<String, HBox> panierItems; // Stocker les éléments du panier pour mise à jour

    public MarcheController() {
        marche = new Marche(); // Instancier le marché
        panierItems = new HashMap<>();
    }

    @FXML
    public void initialize() {
        marche.initRessources();
        remplirListeRessources();

        graineList.setOnMouseClicked(event -> {
            HBox selectedItem = graineList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Label resourceLabel = (Label) selectedItem.getChildren().get(0);
                String ressource = resourceLabel.getText().split(" - ")[0];
                afficherStock(ressource);
            }
        });
    }

    private void remplirListeRessources() {
        graineList.getItems().clear();
        for (String ressource : marche.getRessources().keySet()) {
            double price = marche.getPrix().get(ressource);
            int stock = marche.getRessources().get(ressource);

            HBox itemBox = new HBox(10);
            Label ressourceLabel = new Label(ressource + " - " + price + " pièces (Stock: " + stock + ")");
            Button ajouterButton = new Button("Ajouter");

            if (stock == 0) {
                ajouterButton.setDisable(true);
            }

            ajouterButton.setOnAction(e -> ajouterAuPanier(ressource));

            itemBox.getChildren().addAll(ressourceLabel, ajouterButton);
            graineList.getItems().add(itemBox);
        }
    }

    private void afficherStock(String ressource) {
        int stock = marche.getRessources().get(ressource);
        stockLabel.setText("Stock restant: " + stock);
    }

    private void ajouterAuPanier(String ressource) {
        int stock = marche.getRessources().get(ressource);
        if (stock > 0) {
            marche.getRessources().put(ressource, stock - 1);
        } else {
            return;
        }

        double price = marche.getPrix().get(ressource);

        // Si l'élément existe déjà dans le panier, on incrémente la quantité et le total
        if (panierItems.containsKey(ressource)) {
            HBox existingItem = panierItems.get(ressource);
            Label quantityLabel = (Label) existingItem.getChildren().get(1);
            Label totalLabel = (Label) existingItem.getChildren().get(2);

            int quantity = Integer.parseInt(quantityLabel.getText().substring(1)) + 1;
            quantityLabel.setText("x" + quantity);

            double newTotal = price * quantity;
            totalLabel.setText("Total: " + newTotal + " pièces");
        } else {
            // Créer une HBox pour l'élément ajouté au panier
            HBox panierItem = new HBox(10);
            Label itemLabel = new Label(ressource);
            Label quantityLabel = new Label("x1");
            Label totalLabel = new Label("Total: " + price + " pièces");

            panierItem.getChildren().addAll(itemLabel, quantityLabel, totalLabel);
            panier.getItems().add(panierItem);
            panierItems.put(ressource, panierItem);
        }

        updateBalance();
        remplirListeRessources();
    }

    private void updateBalance() {
        double total = 0;

        for (HBox item : panier.getItems()) {
            Label totalLabel = (Label) item.getChildren().get(2);
            total += Double.parseDouble(totalLabel.getText().split(" ")[1]);
        }

        balanceLabel.setText("Solde: " + total + " pièces");
        totalPanierLabel.setText("Total du panier: " + total + " pièces");
    }

    @FXML
    private void viderPanier() {
        marche.initRessources();
        panier.getItems().clear();
        panierItems.clear();
        remplirListeRessources();
        updateBalance();
    }
}
