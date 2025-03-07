package models;

import java.util.HashMap;
import java.util.Map;

public class Reserve {
    private Map<String, Integer> stock; // Stock des ressources du joueur

    public Reserve() {
        this.stock = new HashMap<>();
    }

    // Ajouter une ressource au stock
    public void ajouterStock(String ressource, int quantite) {
        stock.put(ressource, stock.getOrDefault(ressource, 0) + quantite);
    }

    // Retirer une ressource du stock
    public boolean retirerStock(String ressource, int quantite) {
        if (stock.containsKey(ressource) && stock.get(ressource) >= quantite) {
            stock.put(ressource, stock.get(ressource) - quantite);
            return true;
        }
        return false;
    }

    // Obtenir le stock de ressources
    public Map<String, Integer> getStock() {
        return stock;
    }

    // Méthode pour afficher le stock sous forme de texte
    public String afficherStock() {
        StringBuilder stockText = new StringBuilder("Stock du joueur:\n");
        for (Map.Entry<String, Integer> entry : stock.entrySet()) {
            stockText.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        return stockText.toString();
    }
}
