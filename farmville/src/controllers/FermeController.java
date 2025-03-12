package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Banque;
import models.Stock;
import utils.AlertManager;
import utils.ImageLoader;
import utils.SceneManager;

import java.util.List;
import java.util.Optional;

public class FermeController {

    @FXML
    private Button boutonMarche, boutonReserve, boutonAgrandir;
    @FXML
    private GridPane grilleChamps;

    private GraineController graineController;
    private AnimalsController animalsController;

    private int colonnesActuelles = 10;
    private static final int LIGNES_ACTUELLES = 10;
    private int coutAgrandissement = 500;

    @FXML
    public void initialize() {
        graineController = new GraineController();
        animalsController = new AnimalsController();

        // Récupérer les graines et animaux du stock
        graineController.setGrainesDisponibles(Stock.getInstance().getProduitsReserve());
        animalsController.setAnimauxDisponibles(Stock.getInstance().getProduitsReserve());

        initialiserGrille();
    }

    @FXML
    private void ouvrirMarche() {
        SceneManager.changerScene("/views/Marche.fxml", boutonMarche);
    }

    @FXML
    private void ouvrirReserve() {
        SceneManager.changerScene("/views/Reserve.fxml", boutonReserve);
    }

    private void initialiserGrille() {
        for (int ligne = 1; ligne < LIGNES_ACTUELLES; ligne++) {
            for (int colonne = 0; colonne < colonnesActuelles; colonne++) {
                if (colonne >= 4 && colonne <= 6) continue;
                String imagePath = (colonne < 5) ? "/ressource/image/enclos.jpg" : "/ressource/image/herbes.png";
                ajouterCaseGrille(colonne, ligne, imagePath);
            }
        }
    }

    @FXML
    private void agrandirGrille() {
        Banque banque = Banque.getInstance();

        if (banque.getSolde() < coutAgrandissement) {
            AlertManager.afficherAlerte("Fonds insuffisants", "Vous avez besoin de " + coutAgrandissement + " dollars pour agrandir la ferme.");
            return;
        }

        Optional<String> typeChoisi = demanderTypeExpansion();
        Optional<String> directionChoisie = demanderDirectionExpansion();

        if (typeChoisi.isPresent() && directionChoisie.isPresent()) {
            banque.retirerArgent(coutAgrandissement);
            agrandirSelonDirection(typeChoisi.get(), directionChoisie.get());
            agrandirFenetre();
            coutAgrandissement += 1500;
        }
    }

    private Optional<String> demanderTypeExpansion() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Champ", "Champ", "Enclos");
        dialog.setTitle("Agrandissement de la ferme");
        dialog.setHeaderText("Choisissez le type de terrain à ajouter :");
        dialog.setContentText("Type :");
        return dialog.showAndWait();
    }

    private Optional<String> demanderDirectionExpansion() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Droite", "Droite", "Gauche");
        dialog.setTitle("Choix de la direction");
        dialog.setHeaderText("Choisissez la direction d'expansion :");
        dialog.setContentText("Direction :");
        return dialog.showAndWait();
    }

    private void agrandirSelonDirection(String type, String direction) {
        String imagePath = type.equals("Champ") ? "/ressource/image/herbes.png" : "/ressource/image/enclos.jpg";

        if ("Droite".equals(direction)) {
            ajouterNouvelleColonne(imagePath, true);
        } else {
            ajouterNouvelleColonne(imagePath, false);
        }
        colonnesActuelles++;
    }

    private void ajouterNouvelleColonne(String imagePath, boolean aDroite) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100.0 / (colonnesActuelles + 1));
        grilleChamps.getColumnConstraints().add(col);

        int colonneAjout = aDroite ? colonnesActuelles : 0;
        for (int j = 0; j < LIGNES_ACTUELLES; j++) {
            ajouterCaseGrille(colonneAjout, j, imagePath);
        }
    }

    private void ajouterCaseGrille(int colonne, int ligne, String imagePath) {
        ImageView imageView = creerImageView(imagePath);
        imageView.setOnMouseClicked(event -> {
            if (imagePath.contains("herbes")) {
                ouvrirDialogueChoixGraine(imageView);
            } else {
                ouvrirDialogueChoixAnimal(imageView);
            }
        });
        grilleChamps.add(imageView, colonne, ligne);
    }

    private void agrandirFenetre() {
        Stage stage = (Stage) grilleChamps.getScene().getWindow();
        stage.setWidth(stage.getWidth() + 50);
        stage.setHeight(stage.getHeight() + 50);
    }

    private ImageView creerImageView(String imagePath) {
        ImageView imageView = ImageLoader.chargerImage(imagePath, 0, 0);
        imageView.fitWidthProperty().bind(grilleChamps.widthProperty().divide(colonnesActuelles));
        imageView.fitHeightProperty().bind(grilleChamps.heightProperty().divide(LIGNES_ACTUELLES));
        imageView.setPreserveRatio(false);
        return imageView;
    }

    private void ouvrirDialogueChoixGraine(ImageView champImage) {
        List<String> grainesDisponibles = graineController.getListeNomsGraines();
        if (grainesDisponibles.isEmpty()) {
            AlertManager.afficherAlerte("Aucune graine disponible", "Il n'y a pas de graines disponibles.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(grainesDisponibles.get(0), grainesDisponibles);
        dialog.setTitle("Choisir une graine");
        dialog.setHeaderText("Sélectionnez une graine à planter.");
        dialog.setContentText("Graine :");

        dialog.showAndWait().ifPresent(graineChoisie ->
                graineController.getGraineParNom(graineChoisie).ifPresent(graine ->
                        graineController.planterGraine(graine, champImage)
                )
        );
    }

    private void ouvrirDialogueChoixAnimal(ImageView animalImage) {
        List<String> animauxDisponibles = animalsController.getListeNomsAnimaux();
        if (animauxDisponibles.isEmpty()) {
            AlertManager.afficherAlerte("Aucun animal disponible", "Il n'y a pas d'animaux disponibles.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(animauxDisponibles.get(0), animauxDisponibles);
        dialog.setTitle("Choisir un animal");
        dialog.setHeaderText("Sélectionnez un animal à ajouter dans l'enclos.");
        dialog.setContentText("Animal :");

        dialog.showAndWait().ifPresent(animalChoisi ->
                animalsController.ajouterAnimalEnclos(animalsController.getAnimalParNom(animalChoisi).orElse(null), animalImage)
        );
    }
}
