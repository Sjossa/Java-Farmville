package controlle;

import creation.CreationJardin;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class JardinageController {

    @FXML
    private GridPane zone1;

    @FXML
    private GridPane zone2;

    @FXML
    private GridPane marché;

    private CreationJardin jardin = new CreationJardin();

    @FXML
    public void initialize() {
        jardin.createChamps(zone1, 4, 6);
        jardin.createChamps(zone2, 4, 6);
        jardin.createHome(marché, 1, 1);

    }
}
