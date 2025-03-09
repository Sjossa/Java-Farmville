package controllers;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.ProduitReserve;
import models.Stock;
import utils.ImageLoader;

import java.util.*;

public class GraineController {

    private List<ProduitReserve> grainesDisponibles;
    private final String IMAGE_HERBE = "/ressource/image/herbes.png"; // Image après récolte

    // Map associant chaque graine à ses images de croissance
    private final Map<String, String[]> imagesCroissanceMap = Map.of(
            "Blé", new String[]{"/ressource/image/graine/graine_ble.png", "/ressource/image/graine/graine_ble_2.png", "/ressource/image/graine/graine_ble_3.png", "/ressource/image/graine/graine_ble_4.png"},
            "Carotte", new String[]{"/ressource/image/carotte1.png", "/ressource/image/carotte2.png", "/ressource/image/carotte3.png"},
            "Tomate", new String[]{"/ressource/image/tomate1.png", "/ressource/image/tomate2.png", "/ressource/image/tomate3.png"}
    );

    public GraineController() {
        this.grainesDisponibles = new ArrayList<>();
    }

    public void setGrainesDisponibles(List<ProduitReserve> graines) {
        this.grainesDisponibles = graines;
    }

    public List<String> getListeNomsGraines() {
        List<String> nomsGraines = new ArrayList<>();
        for (ProduitReserve graine : grainesDisponibles) {
            if (graine.getCode().startsWith("GR")) {
                nomsGraines.add(graine.getNom());
            }
        }
        return nomsGraines;
    }

    public ProduitReserve getGraineParNom(String nom) {
        for (ProduitReserve graine : grainesDisponibles) {
            if (graine.getNom().trim().equalsIgnoreCase(nom.trim())) {
                return graine;
            }
        }
        return null;
    }

    public void planterGraine(ProduitReserve graine, ImageView champImage) {
        if (graine.getQuantite() > 0) {
            graine.setQuantite(graine.getQuantite() - 1);

            // Récupérer les images de croissance pour la graine choisie
            String[] imagesCroissance = imagesCroissanceMap.getOrDefault(graine.getNom(), new String[0]);

            if (imagesCroissance.length > 0) {
                afficherCroissance(champImage, imagesCroissance, graine.getNom());
            } else {
                System.out.println("Aucune image de croissance trouvée pour cette graine.");
            }
        } else {
            System.out.println("Pas assez de graines disponibles pour planter.");
        }
    }

    private void afficherCroissance(ImageView champImage, String[] images, String produitFinal) {
        new Thread(() -> {
            for (int i = 0; i < images.length; i++) {
                final int index = i;
                Platform.runLater(() -> champImage.setImage(new Image(getClass().getResourceAsStream(images[index]))));
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // À la fin de la croissance, activer la récolte par un clic
            Platform.runLater(() -> champImage.setOnMouseClicked(event -> recolterProduit(champImage, produitFinal)));
        }).start();
    }

    private void recolterProduit(ImageView champImage, String produitFinal) {
        // Ajouter le produit final dans le stock
        Stock.getInstance().ajouterProduit(new ProduitReserve(produitFinal, "pr_" + produitFinal, 1));

        // Remettre l'image d'herbe et réactiver la plantation
        champImage.setImage(new Image(getClass().getResourceAsStream(IMAGE_HERBE)));

        champImage.setOnMouseClicked(event -> ouvrirDialogueChoixGraine(champImage));
    }

    private void ouvrirDialogueChoixGraine(ImageView champImage) {
        List<String> grainesDisponibles = getListeNomsGraines();

        if (grainesDisponibles.isEmpty()) {
            System.out.println("Aucune graine disponible !");
            return;
        }

        planterGraine(getGraineParNom(grainesDisponibles.get(0)), champImage);
    }
}
