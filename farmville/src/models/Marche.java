package models;

import java.util.HashMap;
import java.util.Map;

public class Marche {
    private Map<String, Integer> ressources = new HashMap<>();
    private Map<String, Double> prix = new HashMap<>();

    // Getter pour les ressources
    public Map<String, Integer> getRessources() {
        return ressources;
    }

    // Getter pour les prix des ressources
    public Map<String, Double> getPrix() {
        return prix;
    }

    // Initialiser les ressources avec une quantité par défaut et un prix
    public void initRessources() {
        ressources.put("Graine de blé", 10);
        ressources.put("Graine de maïs", 10);
        ressources.put("Graine de carotte", 10);
        // Ajouter d'autres ressources si nécessaire

        prix.put("Graine de blé", 2.0);
        prix.put("Graine de maïs", 3.0);
        prix.put("Graine de carotte", 1.5);
        // Ajouter les prix correspondants
    }
}
