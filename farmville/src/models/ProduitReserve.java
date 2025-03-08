package models;

public class ProduitReserve extends Produit {
    private int quantite;

    public ProduitReserve(String nom, String code, int quantite) {
        super(nom, code);  // Appelle le constructeur de la classe parent
        this.quantite = quantite;
    }

    // Getter et setter pour quantite
    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    @Override
    public void afficherDetails() {
        System.out.println("Produit Reserve - Nom: " + getNom() + ", Code: " + getCode() + ", Quantité: " + getQuantite());
    }
}
