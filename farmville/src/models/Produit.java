package models;

public abstract class Produit {
    protected int age;
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
}
