package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import models.Banque;
import models.Stock;
import utils.AlertManager;
import utils.ImageLoader;
import utils.SceneManager;
import controllers.MarcheController;

import java.util.List;
import java.util.Optional;

public class FermeController {

    @FXML
    private Button boutonMarche, boutonReserve, boutonAgrandir;
    @FXML
    private GridPane grilleChamps;
    @FXML
    private Label labelSolde;

    private GraineController graineController;
    private AnimalsController animalsController;

    private int colonnesActuelles = 10;
    private static final int LIGNES_ACTUELLES = 10;
    private int coutAgrandissement = 500;

    @FXML
    public void initialize() {
        graineController = new GraineController();
        animalsController = new AnimalsController();

        mettreAJourSolde();
        Banque.getInstance().soldeProperty().addListener((obs, oldVal, newVal) -> mettreAJourSolde());
        // Initialiser les ressources depuis le stock
        graineController.setGrainesDisponibles(Stock.getInstance().getProduitsReserve());
        animalsController.setAnimauxDisponibles(Stock.getInstance().getProduitsReserve());

        initialiserGrille();
    }

    @FXML
    private void ouvrirMarche() {
        SceneManager.ouvrirNouvelleFenetre("/views/Marche.fxml", "Marché");
    }

    @FXML
    private void ouvrirReserve() {
        SceneManager.ouvrirNouvelleFenetre("/views/Reserve.fxml", "Réserve");
    }

    private void mettreAJourSolde() {
        labelSolde.setText("Solde : " + Banque.getInstance().getSolde() + " pièces");
    }

    private void initialiserGrille() {
        for (int ligne = 1; ligne < LIGNES_ACTUELLES; ligne++) {
            for (int colonne = 0; colonne < colonnesActuelles; colonne++) {
                if (colonne >= 5 && colonne <= 4) continue;
                ajouterCaseGrille(colonne, ligne, colonne < 5 ? "/ressource/image/enclos.jpg" : "/ressource/image/herbes.png");
            }
        }
    }

    @FXML
    private void agrandirGrille() {
        Banque banque = Banque.getInstance();

        if (banque.getSolde() < coutAgrandissement) {
            AlertManager.afficherAlerte("Fonds insuffisants", "Vous avez besoin de $" + coutAgrandissement + " pour agrandir la ferme.");
            return;
        }

        Optional<String> typeChoisi = demanderChoix("Agrandissement de la ferme", "Choisissez le type de terrain à ajouter :", "Type :", "Champ", "Enclos");
        Optional<String> directionChoisie = demanderChoix("Choix de la direction", "Choisissez la direction d'expansion :", "Direction :", "Droite", "Gauche");

        if (typeChoisi.isPresent() && directionChoisie.isPresent()) {
            banque.retirerArgent(coutAgrandissement);
            agrandirSelonDirection(typeChoisi.get(), directionChoisie.get());
            agrandirFenetre();
            coutAgrandissement += 1500;
        }
    }

    private Optional<String> demanderChoix(String titre, String header, String content, String... options) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options[0], options);
        dialog.setTitle(titre);
        dialog.setHeaderText(header);
        dialog.setContentText(content);
        return dialog.showAndWait();
    }

    private void agrandirSelonDirection(String type, String direction) {
        String imagePath = type.equals("Champ") ? "/ressource/image/herbes.png" : "/ressource/image/enclos.jpg";
        ajouterNouvelleColonne(imagePath, "Droite".equals(direction));
        colonnesActuelles++;
    }
    @FXML
    private void hoverEffect(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #ff7043; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10px; -fx-padding: 5px 15px;");
    }

    @FXML
    private void resetEffect(MouseEvent event) {
        ((Button) event.getSource()).setStyle("-fx-background-color: #ff5722; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10px; -fx-padding: 5px 15px;");
    }

    private void ajouterNouvelleColonne(String imagePath, boolean aDroite) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(100.0 / (colonnesActuelles + 1));
        grilleChamps.getColumnConstraints().add(col);

        int colonneAjout = aDroite ? colonnesActuelles : 0;
        for (int j = 1; j < LIGNES_ACTUELLES; j++) {
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

        demanderChoix("Choisir une graine", "Sélectionnez une graine à planter.", "Graine :", grainesDisponibles.toArray(new String[0]))
                .ifPresent(graineChoisie ->
                        graineController.getGraineParNom(graineChoisie)
                                .ifPresent(graine -> graineController.planterGraine(graine, champImage))
                );
    }

    private void ouvrirDialogueChoixAnimal(ImageView animalImage) {
        List<String> animauxDisponibles = animalsController.getListeNomsAnimaux();
        if (animauxDisponibles.isEmpty()) {
            AlertManager.afficherAlerte("Aucun animal disponible", "Il n'y a pas d'animaux disponibles.");
            return;
        }

        demanderChoix("Choisir un animal", "Sélectionnez un animal à ajouter dans l'enclos.", "Animal :", animauxDisponibles.toArray(new String[0]))
                .ifPresent(animalChoisi ->
                        animalsController.getAnimalParNom(animalChoisi)
                                .ifPresent(animal -> animalsController.ajouterAnimalEnclos(animal, animalImage))
                );
    }
}
