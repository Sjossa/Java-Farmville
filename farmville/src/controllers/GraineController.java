package controllers;

import javafx.scene.image.ImageView;
import models.ProduitReserve;
import models.Stock;

import java.util.ArrayList;
import java.util.List;

public class GraineController {

    private List<ProduitReserve> grainesDisponibles;

    public GraineController() {
        this.grainesDisponibles = new ArrayList<>();
    }

    // Retourne la liste des graines disponibles
    public List<ProduitReserve> getGrainesDisponibles() {
        return grainesDisponibles;
    }

    // Met à jour la liste des graines disponibles
    public void setGrainesDisponibles(List<ProduitReserve> graines) {
        grainesDisponibles = graines;
    }

    // Retourne la liste des noms des graines disponibles dans le stock
    public List<String> getListeNomsGraines() {
        List<String> nomsGraines = new ArrayList<>();
        for (ProduitReserve graine : grainesDisponibles) {
            if (graine.getCode().startsWith("GR")) {
                nomsGraines.add(graine.getNom());
            }
        }
        return nomsGraines;
    }

    // Retourne une graine par son nom
    public ProduitReserve getGraineParNom(String nom) {
        for (ProduitReserve graine : grainesDisponibles) {
            if (graine.getNom().trim().equalsIgnoreCase(nom.trim())) {
                return graine;
            }
        }
        return null;
    }

    // Méthode pour planter une graine
    public void planterGraine(ProduitReserve graine, ImageView champImage, String[] imagesCroissance, String produitFinal) {
        if (graine != null && graine.getQuantite() > 0) {
            // Décrémenter la quantité de la graine dans le stock
            graine.setQuantite(graine.getQuantite() - 1);

            // Afficher les images en fonction de la croissance de la graine
            champImage.setImage(new javafx.scene.image.Image(getClass().getResource(imagesCroissance[0]).toExternalForm()));

            // Si la graine atteint sa forme finale, ajouter le produit final à la réserve
            Stock.getInstance().ajouterProduit(new ProduitReserve(produitFinal, "pr_" + produitFinal, 1));
        } else {
            System.out.println("Pas assez de graines disponibles pour planter.");
        }
    }
}
