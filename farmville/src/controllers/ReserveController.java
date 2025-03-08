package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ProduitReserve;
import models.Stock;

public class ReserveController {

    @FXML
    private TableView<ProduitReserve> stockTable;
    @FXML
    private TableColumn<ProduitReserve, String> productNameColumn;
    @FXML
    private TableColumn<ProduitReserve, String> productCodeColumn;
    @FXML
    private TableColumn<ProduitReserve, Integer> stockQuantityColumn;

    @FXML
    public void initialize() {
        configurerColonnes();
        rafraichirStock();
    }

    private void configurerColonnes() {
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        productCodeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        stockQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantite"));
    }

    public void rafraichirStock() {
        stockTable.setItems(Stock.getInstance().getProduitsReserve());
    }
}
