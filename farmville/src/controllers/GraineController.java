package controllers;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.ProduitReserve;
import models.Stock;
import utils.AlertManager;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GraineController {

    private List<ProduitReserve> grainesDisponibles;
    private final String IMAGE_HERBE = "/ressource/image/herbes.png";

    private final Map<String, String[]> imagesCroissanceMap = Map.of(
            "Blé", new String[]{"/ressource/image/graine/graine_ble.jpg", "/ressource/image/graine/graine_ble_2.png", "/ressource/image/graine/graine_ble_3.png", "/ressource/image/graine/graine_ble_4.png"},
            "Carotte", new String[]{"/ressource/image/graine/ressource/image/graine/carotte1.png", "/ressource/image/graine/carotte2.png", "/ressource/image/graine/carotte3.png","/ressource/image/graine/carotte4.png"}
    );

    public GraineController() {
        setGrainesDisponibles(Stock.getInstance().getProduitsReserve()
                .stream()
                .filter(p -> p.getCode().startsWith("GRA"))
                .collect(Collectors.toList()));
    }

    public void setGrainesDisponibles(List<ProduitReserve> graines) {
        this.grainesDisponibles = graines;
    }

    public List<String> getListeNomsGraines() {
        return grainesDisponibles.stream()
                .map(ProduitReserve::getNom)
                .collect(Collectors.toList());
    }

    public Optional<ProduitReserve> getGraineParNom(String nom) {
        return grainesDisponibles.stream()
                .filter(graine -> graine.getNom().equalsIgnoreCase(nom))
                .findFirst();
    }

    public void planterGraine(ProduitReserve graine, ImageView champImage) {
        if (graine != null && graine.getQuantite() > 0) {
            graine.setQuantite(graine.getQuantite() - 1);
            String[] imagesCroissance = imagesCroissanceMap.get(graine.getNom());

            if (imagesCroissance != null) {
                afficherCroissance(champImage, imagesCroissance, graine.getNom());
            } else {
                AlertManager.afficherAlerte("Erreur", "Aucune image de croissance trouvée.");
            }
        } else {
            AlertManager.afficherAlerte("Erreur", "Pas assez de graines disponibles.");
        }
    }

    private void afficherCroissance(ImageView champImage, String[] images, String produitFinal) {
        Thread croissanceThread = new Thread(() -> {
            try {
                for (String imagePath : images) {
                    Platform.runLater(() -> champImage.setImage(new Image(getClass().getResourceAsStream(imagePath))));
                    Thread.sleep(2000); // Pause de 2 secondes entre chaque image
                }

                // Une fois la croissance terminée, activer la récolte (sans collecte automatique)
                Platform.runLater(() -> activerRecolte(champImage, produitFinal));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Le thread de croissance a été interrompu.");
            }
        });

        croissanceThread.setDaemon(true); // Le thread s'arrête si l'application ferme
        croissanceThread.start();
    }

    private void activerRecolte(ImageView champImage, String produitFinal) {
        champImage.setOnMouseClicked(event -> recolterProduit(champImage, produitFinal));
    }

    private void recolterProduit(ImageView champImage, String produitFinal) {
        String codeProduit = "PR_" + produitFinal;
        ProduitReserve produitRecolte = new ProduitReserve(produitFinal, codeProduit, 1);

        System.out.println("Récolte de " + produitFinal + " avec code " + codeProduit);
        Stock.getInstance().ajouterProduit(produitRecolte);

        champImage.setImage(new Image(getClass().getResourceAsStream(IMAGE_HERBE)));
        champImage.setOnMouseClicked(event -> ouvrirDialogueChoixGraine(champImage));
    }

    private void ouvrirDialogueChoixGraine(ImageView champImage) {
        List<String> graines = getListeNomsGraines();
        if (!graines.isEmpty()) {
            getGraineParNom(graines.get(0)).ifPresent(graine -> planterGraine(graine, champImage));
        } else {
            AlertManager.afficherAlerte("Erreur", "Aucune graine disponible !");
        }
    }
}
