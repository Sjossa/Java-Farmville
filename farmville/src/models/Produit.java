package models;

public abstract class Produit {
    private String nom;
    private String code;

    public Produit(String nom, String code) {
        this.nom = nom;
        this.code = code;
    }

    // Méthodes getter
    public String getNom() {
        return nom;
    }

    public String getCode() {
        return code;
    }

    // Méthode abstraite à implémenter dans les classes dérivées
    public abstract void afficherDetails();
}
