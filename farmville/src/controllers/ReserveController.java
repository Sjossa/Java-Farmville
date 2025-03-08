package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Produit;
import models.ProduitMarche;
import models.ProduitReserve;

public class ReserveController {

    @FXML
    private TextField searchField;

    @FXML
    private Label inputPreviewLabel;

    @FXML
    private TableView<Produit> stockTable;

    @FXML
    private TableColumn<Produit, String> productNameColumn;

    @FXML
    private TableColumn<Produit, String> productCodeColumn;

    @FXML
    private TableColumn<Produit, Integer> stockQuantityColumn;

    private ObservableList<Produit> stockData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Associer les colonnes aux propriétés du modèle Produit
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        productCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        stockQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));

        // Ajouter des produits de test
        stockData.addAll(
                new ProduitReserve("Blé", "GR001", 50),
                new ProduitReserve("Maïs", "GR002", 30),
                new ProduitReserve("Vache", "AN001", 5),
                new ProduitReserve("Poulet", "AN002", 20)
        );

        stockTable.setItems(stockData);

        // Écouter l'entrée dans le champ de recherche
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            inputPreviewLabel.setText("Recherche : " + newValue);
            filtrerStock(newValue);
        });
    }

    private void filtrerStock(String recherche) {
        if (recherche.isEmpty()) {
            stockTable.setItems(stockData);
        } else {
            ObservableList<Produit> filtres = FXCollections.observableArrayList();
            for (Produit p : stockData) {
                if (p.getNom().toLowerCase().contains(recherche.toLowerCase()) ||
                        p.getCode().toLowerCase().contains(recherche.toLowerCase())) {
                    filtres.add(p);
                }
            }
            stockTable.setItems(filtres);
        }
    }
}
