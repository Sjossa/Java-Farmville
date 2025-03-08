package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Stock {
    private static Stock instance; // Singleton pour une seule instance partagée
    private final ObservableList<ProduitReserve> produitsReserve;

    private Stock() {
        this.produitsReserve = FXCollections.observableArrayList();
    }

    // Retourne l'instance unique de Stock
    public static Stock getInstance() {
        if (instance == null) {
            instance = new Stock();
        }
        return instance;
    }

    // Ajouter un produit à la réserve
    public void ajouterProduit(ProduitReserve produit) {
        produitsReserve.add(produit);
    }

    // Récupérer la liste des produits dans la réserve
    public ObservableList<ProduitReserve> getProduitsReserve() {
        return produitsReserve;
    }
}
