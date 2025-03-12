package models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Stock {
    private static Stock instance;
    private final ObservableList<ProduitReserve> produitsReserve;

    private Stock() {
        this.produitsReserve = FXCollections.observableArrayList();
    }

    public static Stock getInstance() {
        if (instance == null) {
            instance = new Stock();
        }
        return instance;
    }

    public void ajouterProduit(ProduitReserve produit) {
        ProduitReserve produitExistant = getProduitReserve(produit.getCode());

        if (produitExistant != null) {
            produitExistant.setQuantite(produitExistant.getQuantite() + produit.getQuantite());
            if (produitExistant.getQuantite() <= 0) {
                produitsReserve.remove(produitExistant);
            }
        } else {
            if (produit.getQuantite() > 0) {
                produitsReserve.add(produit);
            }
        }
    }

    public ObservableList<ProduitReserve> getProduitsReserve() {
        return produitsReserve;
    }

    public ProduitReserve getProduitReserve(String nom) {
        return produitsReserve.stream()
                .filter(produit -> produit.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }
}
