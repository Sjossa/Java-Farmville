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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AnimalsController {

    private List<ProduitReserve> animauxDisponibles;
    private static final String IMAGE_ENCLOS_VIDE = "/ressource/image/enclos.jpg";
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final Map<String, String[]> imagesCroissanceMap = Map.of(
            "Poule", new String[]{"/ressource/image/Animals/poule_1.png", "/ressource/image/Animals/poule_2.png", "/ressource/image/Animals/poule_3.png"},
            "Vache", new String[]{"/ressource/image/Animals/vache_1.jpg", "/ressource/image/Animals/vache_2.jpg"}
    );

    public AnimalsController() {
        setAnimauxDisponibles(Stock.getInstance().getProduitsReserve()
                .stream()
                .filter(p -> p.getCode().startsWith("ANI"))
                .collect(Collectors.toList()));
    }

    public void setAnimauxDisponibles(List<ProduitReserve> animaux) {
        this.animauxDisponibles = animaux;
    }

    public List<String> getListeNomsAnimaux() {
        return animauxDisponibles.stream()
                .map(ProduitReserve::getNom)
                .collect(Collectors.toList());
    }

    public Optional<ProduitReserve> getAnimalParNom(String nom) {
        return animauxDisponibles.stream()
                .filter(animal -> animal.getNom().equalsIgnoreCase(nom))
                .findFirst();
    }

    public void ajouterAnimalEnclos(ProduitReserve animal, ImageView enclosImage) {
        if (animal.getQuantite() > 0) {
            animal.setQuantite(animal.getQuantite() - 1);
            String[] imagesCroissance = imagesCroissanceMap.get(animal.getNom());

            if (imagesCroissance != null) {
                afficherCroissance(enclosImage, imagesCroissance, animal);
            } else {
                AlertManager.afficherAlerte("Erreur", "Aucune image de croissance trouvée.");
            }
        } else {
            AlertManager.afficherAlerte("Erreur", "Il n'y a pas d'animaux disponibles.");
        }
    }

    private void afficherCroissance(ImageView enclosImage, String[] images, ProduitReserve animal) {
        final int[] index = {0};

        scheduler.scheduleAtFixedRate(() -> {
            if (index[0] < images.length) {
                Platform.runLater(() -> enclosImage.setImage(new Image(getClass().getResourceAsStream(images[index[0]]))));
                index[0]++;
            } else {
                commencerProductionRessource(enclosImage, animal);
            }
        }, 0, 2, TimeUnit.SECONDS);
    }

    private void commencerProductionRessource(ImageView enclosImage, ProduitReserve animal) {
        scheduler.scheduleAtFixedRate(() -> {
            if (animal.getAge() >= 100) {
                Platform.runLater(() -> {
                    AlertManager.afficherAlerte("Fin de vie", "L'animal est mort.");
                    setImage(enclosImage, IMAGE_ENCLOS_VIDE);
                });
                stop();
                return;
            }

            if ("Vache".equals(animal.getNom())) {
                Stock.getInstance().ajouterProduit(new ProduitReserve("Lait", "PR_LAIT", 1));
            }

            animal.setAge(animal.getAge() + 10);
        }, 0, 5, TimeUnit.SECONDS);
    }

    private void setImage(ImageView imageView, String path) {
        Optional.ofNullable(getClass().getResourceAsStream(path))
                .ifPresent(inputStream -> Platform.runLater(() -> imageView.setImage(new Image(inputStream))));
    }

    public void stop() {
        scheduler.shutdown();
    }
}
