package models;

public class Banque {
    private static Banque instance;
    private double solde;

    private Banque() {
        this.solde = 1000; // Solde initial
    }

    public static Banque getInstance() {
        if (instance == null) {
            instance = new Banque();
        }
        return instance;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public void ajouterArgent(double montant) {
        solde += montant;
    }

    public boolean retirerArgent(double montant) {
        if (solde >= montant) {
            solde -= montant;
            return true;
        }
        return false;
    }
}
