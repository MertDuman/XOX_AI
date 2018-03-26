package pvppackage;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class XoXController {
    @FXML Button button1 = new Button();
    @FXML Button button2 = new Button();
    @FXML Button button3 = new Button();
    @FXML Button button4 = new Button();
    @FXML Button button5 = new Button();
    @FXML Button button6 = new Button();
    @FXML Button button7 = new Button();
    @FXML Button button8 = new Button();
    @FXML Button button9 = new Button();
    @FXML Text playerText = new Text();
    private int turn = 1;
    private Button buttons[] = new Button[9];

    public void initialize() {
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
        buttons[4] = button5;
        buttons[5] = button6;
        buttons[6] = button7;
        buttons[7] = button8;
        buttons[8] = button9;
        playerText.setText("Player 1");
    }

    @FXML
    public void handleButtonClick(ActionEvent event) {
        Button currButton = (Button)event.getSource();
        if(currButton.getText().isEmpty()) {
            if (turn % 2 == 1) {
                currButton.setText("X");
                turn++;
                playerText.setText("Player 2");
            } else if (turn % 2 == 0) {
                currButton.setText("O");
                turn++;
                playerText.setText("Player 1");
            }

            checkForGameOver();
        }
    }

    private void checkForGameOver() {
        // 8 ways to win
        boolean Xarray[] = new boolean[9];
        boolean Oarray[] = new boolean[9];

        for ( int i = 0; i < 9; i++) {
            if(buttons[i].getText().equals("X")) {
                Xarray[i] = true;
            }
            if(buttons[i].getText().equals("O")) {
                Oarray[i] = true;
            }
        }

        if(Xarray[0] && Xarray[1] && Xarray[2]) {
            showGameOver(0);
        } else if(Xarray[3] && Xarray[4] && Xarray[5]) {
            showGameOver(0);
        } else if(Xarray[6] && Xarray[7] && Xarray[8]) {
            showGameOver(0);
        } else if(Xarray[0] && Xarray[3] && Xarray[6]) {
            showGameOver(0);
        } else if(Xarray[1] && Xarray[4] && Xarray[7]) {
            showGameOver(0);
        } else if(Xarray[2] && Xarray[5] && Xarray[8]) {
            showGameOver(0);
        } else if(Xarray[0] && Xarray[4] && Xarray[8]) {
            showGameOver(0);
        } else if(Xarray[2] && Xarray[4] && Xarray[6]) {
            showGameOver(0);
        }

        if(Oarray[0] && Oarray[1] && Oarray[2]) {
            showGameOver(1);
        } else if(Oarray[3] && Oarray[4] && Oarray[5]) {
            showGameOver(1);
        } else if(Oarray[6] && Oarray[7] && Oarray[8]) {
            showGameOver(1);
        } else if(Oarray[0] && Oarray[3] && Oarray[6]) {
            showGameOver(1);
        } else if(Oarray[1] && Oarray[4] && Oarray[7]) {
            showGameOver(1);
        } else if(Oarray[2] && Oarray[5] && Oarray[8]) {
            showGameOver(1);
        } else if(Oarray[0] && Oarray[4] && Oarray[8]) {
            showGameOver(1);
        } else if(Oarray[2] && Oarray[4] && Oarray[6]) {
            showGameOver(1);
        }

        if(turn >= 10) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText("");
            alert.setContentText("Draw");
            alert.showAndWait();

            Platform.exit();
            System.exit(0);
        }
    }

    private void showGameOver( int winner) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("");
        if(winner == 0) {
            alert.setContentText("X won!");
        } else {
            alert.setContentText("O won!");
        }
        alert.showAndWait();

        Platform.exit();
        System.exit(0);
    }
}
