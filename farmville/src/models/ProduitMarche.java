package models;
public class ProduitMarche {

    private String nom;
    private String id;
    private double prix;

    public ProduitMarche(String nom, String id, double prix) {
        this.nom = nom;
        this.id = id;
        this.prix = prix;
    }

    public String getNom() {
        return nom;
    }

    public String getId() {
        return id;
    }

    public double getPrix() {
        return prix;
    }

    @Override
    public String toString() {
        return nom;
    }
}
