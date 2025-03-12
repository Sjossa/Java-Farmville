package models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Banque {
    private static final Banque instance = new Banque();
    private final DoubleProperty solde = new SimpleDoubleProperty(1000); // Solde initial

    private Banque() {}

    public static Banque getInstance() {
        return instance;
    }

    public double getSolde() {
        return solde.get();
    }

    public void ajouterArgent(double montant) {
        solde.set(solde.get() + montant);
    }

    public boolean retirerArgent(double montant) {
        if (solde.get() >= montant) {
            solde.set(solde.get() - montant);
            return true;
        }
        return false;
    }

    public DoubleProperty soldeProperty() {
        return solde;
    }
}
