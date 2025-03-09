package models;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.TimerTask;

public class CroissanceTask extends TimerTask {
    private ImageView imageView;
    private String[] imagesCroissance;
    private int etape = 0;
    private ProduitReserve graine;
    private String produitFinal;
    private Stock stock;

    public CroissanceTask(ImageView imageView, String[] imagesCroissance, ProduitReserve graine, String produitFinal, Stock stock) {
        this.imageView = imageView;
        this.imagesCroissance = imagesCroissance;
        this.graine = graine;
        this.produitFinal = produitFinal;
        this.stock = stock;
    }

    @Override
    public void run() {
        if (etape < imagesCroissance.length) {
            imageView.setImage(new Image(imagesCroissance[etape]));
            etape++;
        } else {
            // Plante mature -> Récolte possible
            this.cancel();
            stock.ajouterProduit(new ProduitReserve(produitFinal, "pr_" + produitFinal, 1));
            // Il faudra appeler la méthode d'affichage d'alerte depuis le contrôleur ici
        }
    }
}
