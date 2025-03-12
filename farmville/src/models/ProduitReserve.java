package models;

public class ProduitReserve extends Produit {
    private int quantite;
    private double prix;
    private int age;

    public ProduitReserve(String nom, String code, int quantite) {
        super(nom, code);
        this.quantite = quantite;
        this.prix = trouverPrixProduit(nom);
        this.age = code.startsWith("ANI") ? 0 : -1;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public double getPrix() {
        return prix;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void afficherDetails() {
        System.out.println("Produit Réserve - Nom: " + getNom() + ", Code: " + getCode() +
                ", Quantité: " + getQuantite() + ", Prix: " + getPrix() +
                ", Âge: " + (age != -1 ? age : "N/A"));
    }

    private double trouverPrixProduit(String produitNom) {
        switch (produitNom) {
            case "Blé": return 10.0;
            case "Maïs": return 12.0;
            case "Vache": return 100.0;
            case "Poulet": return 40.0;
            default: return 0.0;
        }
    }
}
