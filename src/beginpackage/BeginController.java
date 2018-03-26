package beginpackage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Mert Duman
 * @version 24.12.2017
 */
public class BeginController {
    @FXML Button PvPButton = new Button();
    @FXML Button PvEButton = new Button();

    public void initialize() {

    }

    @FXML
    public void playPvE() throws IOException {
        GridPane root = FXMLLoader.load( getClass().getResource("/pvepackage/aixox.fxml"));
        Scene aixoxScene = new Scene(root, 600, 600);
        Stage aixoxStage = new Stage();
        aixoxStage.setScene(aixoxScene);
        aixoxStage.show();

        Stage curr = (Stage)PvEButton.getScene().getWindow();
        curr.close();
    }

    @FXML
    public void playPvP() throws IOException {
        GridPane root = FXMLLoader.load( getClass().getResource("/pvppackage/xox.fxml"));
        Scene xoxScene = new Scene(root, 600, 600);
        Stage xoxStage = new Stage();
        xoxStage.setScene( xoxScene);
        xoxStage.show();

        Stage curr = (Stage)PvPButton.getScene().getWindow();
        curr.close();
    }
}
