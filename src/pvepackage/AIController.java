package pvepackage;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.util.Optional;
import java.util.Random;


/**
 * @author Mert Duman
 * @version 21.03.2018
 * Different from the never losing AI's found on the internet since it doesn't use the recursive minimax algorithm.
 * I've wanted to try this as a challenge for myself to recreate a never losing AI without the use of this algorithm.
 * The reason being is, with minimax, AI feels too much like an AI.
 * That means it will always play in the same pattern, no matter how many times you make the same move,
 * even though it's not necessary for 'not losing'.
 * The game board is a square, so essentially, symmetrical moves are all correct. For example, when the player goes middle,
 * going either one of the corners is the correct move.
 * However, a minimax AI will always pick the top left corner. I believe this makes the game dull and not entertaining
 * for the player because the player is easily convinced that he can't win. There is no variety.
 * This recreation of the AI makes seemingly different moves each turn, even if the player makes the same move multiple times.
 * It will still not lose, but will not follow the same pattern all the time.
 * As already stated, it's a 'non-losing' AI, meaning it won't plan to win (but still win if given the chance) but to draw.
 * Enjoy.
 */
public class AIController {
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

    private Button buttons[] = new Button[9];
    private boolean Xarray[][];
    private boolean Oarray[][];
    private boolean aiArray[][];
    private boolean playerArray[][];

    private boolean isPlayerTurn;
    private boolean playerWentFirst;

    private Random random = new Random();

    private String playerMark;
    private String aiMark;

    private int turn;

    public void initialize() {
        turn = 1;
        Xarray = new boolean[3][3];
        Oarray = new boolean[3][3];
        setButtons();
        promptWhoGoesFirst();
        setMarksAndArrays();

        // Play the first turn if it's not player's
        if(!playerWentFirst) {
            handleAITurn( null);
        }
    }

    @FXML
    /**
     * Checks if the pressed button is empty. Checks if it is the player's turn.
     * If both are true, advances the game.
     * @param event Event received from the button pressed by the user.
     */
    public void handleButtonClick(ActionEvent event) {
        Button currButton = (Button)event.getSource();
        int currButtonID = Integer.parseInt( currButton.getId());

        if(currButton.getText().isEmpty()) {
            if(isPlayerTurn) {
                currButton.setText( playerMark);
                fillXOArrays( currButtonID);
                turn++;
                if(checkForGameOver()) {
                    return;
                }

                isPlayerTurn = false;

                handleAITurn( currButton);

                checkForTurnsOver();
            }
        }
    }

    /**
     * If the AI is going first, not losing is simple, but only needs to account for cases
     * where the player might try to fool AI by not playing reasonably.
     * If the player is going first, AI needs to properly respond.
     * @param playerButton Button the player pressed this turn.
     */
    private void handleAITurn( Button playerButton) {
        if(!playerWentFirst) {
            /* Check for win */
            if(checkForAIWin()) {
                return;
            }

            /* Check for not lose */
            if( !checkForAINotLose() ) {
                /* Play if not above */
                AIrespondEasy( playerButton); // generate between 0-8
            }

            isPlayerTurn = true;
            turn++;
        } else {
            /* Check for win */
            if(checkForAIWin()) {
                return;
            }

            /* Check for not lose */
            if( !checkForAINotLose() ) {
                /* Play if not above */
                AIrespond( playerButton);
            }

            isPlayerTurn = true;
            turn++;
        }
    }

    /**
     * Checks each row, column and cross for 2 AI marks.
     * If there are 2 marks in any of these, checks if the third spot is empty.
     * If so, wins.
     * @return return true if the game is over, false if not. Needed for the cases we reset the game.
     */
    private boolean checkForAIWin() {
        // if in any row, column or cross there are 2 marks, win
        int marks = 0;
        int emptyLoc = 0;
        for(int i = 0; i < 3; i++) {
            // Checks each row for a win
            for(int j = 0; j < 3; j++) {
                if(aiArray[i][j]) {
                    marks++;
                } else {
                    emptyLoc = j;
                }
            }
            if(marks == 2 && !playerArray[i][emptyLoc]) {
                int buttonLoc = (i * 3) + emptyLoc;
                buttons[buttonLoc].setText( aiMark);
                fillXOArrays(buttonLoc + 1);

                return showGameOver("AI won");
            }
            marks = 0;
            emptyLoc = 0;

            // Checks each column for a win
            for(int j = 0; j < 3; j++) {
                if(aiArray[j][i]) {
                    marks++;
                } else {
                    emptyLoc = j;
                }
            }
            if(marks == 2 && !playerArray[emptyLoc][i]) {
                int buttonLoc = (emptyLoc * 3) + i;
                buttons[buttonLoc].setText( aiMark);
                fillXOArrays(buttonLoc + 1);

                return showGameOver("AI won");
            }
            marks = 0;
            emptyLoc = 0;
        }

        // Check the crosses
            // left to right, cross 1
        for(int i = 0; i < 3; i++) {
            if(aiArray[i][i]) {
                marks++;
            } else {
                emptyLoc = i;
            }
        }
        if(marks == 2 && !playerArray[emptyLoc][emptyLoc]) {
            int buttonLoc = (emptyLoc * 3) + emptyLoc;
            buttons[buttonLoc].setText( aiMark);
            fillXOArrays(buttonLoc + 1);

            return showGameOver("AI won");
        }
        marks = 0;
        emptyLoc = 0;

            // right to left, cross 2
        for(int i = 0; i < 3; i++) {
            if(aiArray[i][2 - i]) {
                marks++;
            } else {
                emptyLoc = i;
            }
        }
        if(marks == 2 && !playerArray[emptyLoc][2 - emptyLoc]) {
            int buttonLoc = (emptyLoc * 3) + (2 - emptyLoc);
            buttons[buttonLoc].setText( aiMark);
            fillXOArrays(buttonLoc + 1);

            return showGameOver("AI won");
        }

        return false;
    }

    /**
     * Checks each row, column and cross for 2 player marks.
     * If there are 2 marks in any of these, checks if the third spot is empty.
     * If so, defends.
     * @return return true if defended, false if no need to defend.
     */
    private boolean checkForAINotLose() {
        boolean aiArray[][];
        boolean playerArray[][];
        aiArray = playerWentFirst ?  Oarray :  Xarray;
        playerArray = playerWentFirst ?  Xarray :  Oarray;

        // if in any row, column or cross there are 2 marks, defend
        int marks = 0;
        int emptyLoc = 0;
        for(int i = 0; i < 3; i++) {
            // Checks each row to defend
            for(int j = 0; j < 3; j++) {
                if(playerArray[i][j]) {
                    marks++;
                } else {
                    emptyLoc = j;
                }
            }
            if(marks == 2 && !aiArray[i][emptyLoc]) {
                int buttonLoc = (i * 3) + emptyLoc;
                buttons[buttonLoc].setText( aiMark);
                fillXOArrays(buttonLoc + 1);

                return true;
            }
            marks = 0;
            emptyLoc = 0;

            // Checks each column to defend
            for(int j = 0; j < 3; j++) {
                if(playerArray[j][i]) {
                    marks++;
                } else {
                    emptyLoc = j;
                }
            }
            if(marks == 2 && !aiArray[emptyLoc][i]) {
                int buttonLoc = (emptyLoc * 3) + i;
                buttons[buttonLoc].setText( aiMark);
                fillXOArrays(buttonLoc + 1);

                return true;
            }
            marks = 0;
            emptyLoc = 0;
        }

        // Check the crosses
        for(int i = 0; i < 3; i++) {
            if(playerArray[i][i]) {
                marks++;
            } else {
                emptyLoc = i;
            }
        }
        if(marks == 2 && !aiArray[emptyLoc][emptyLoc]) {
            int buttonLoc = (emptyLoc * 3) + emptyLoc;
            buttons[buttonLoc].setText( aiMark);
            fillXOArrays(buttonLoc + 1);

            return true;
        }
        marks = 0;
        emptyLoc = 0;

        for(int i = 0; i < 3; i++) {
            if(playerArray[i][2 - i]) {
                marks++;
            } else {
                emptyLoc = i;
            }
        }
        if(marks == 2 && !aiArray[emptyLoc][2 - emptyLoc]) {
            int buttonLoc = (emptyLoc * 3) + (2 - emptyLoc);
            buttons[buttonLoc].setText( aiMark);
            fillXOArrays(buttonLoc + 1);

            return true;
        }

        return false;
    }

    /**
     * Responsible for how the AI responds to the player when AI is going first.
     * These responses are done after checking for winning and NOT losing.
     * @param playerButton Button the player pressed this turn.
     */
    private void AIrespondEasy( Button playerButton) {
        /* Cases where the AI randomly plays on an edge are important only */
        if (aiArray[0][1] || aiArray[1][0] || aiArray[1][2] || aiArray[2][1]) {
            // If by turn 3 (AIs second turn) player has played the middle and AI played on an edge, go corner to not lose.
            if (turn == 3 && playerArray[1][1]) {
                AIrandomPlay(0);
                return;
            }
            // This is generally only called if the player goes out of a reasonable play-style to try and see
            // if the AI is really unbeatable. Secures most cases and occasionally ensures a victory for AI.
            if (turn == 5) {
                AIrandomPlay(0);
                return;
            }
        }

        // Just like above, secures most cases by taking mid
        if (turn == 3 && !playerArray[1][1] && !aiArray[1][1]) {
            AIspecificPlay(4);
            return;
        }

        AIrandomPlay(2);
    }

    /**
     * Main function that drives the game. Responsible for how the AI responds to the player when player is going first.
     * These responses are done after checking for winning and NOT losing.
     * Check comments for explanations.
     * @param playerButton Button the player pressed this turn.
     */
    private void AIrespond( Button playerButton) {
        // 2 tricks to always win
            // Go middle, opponent goes edge, always win by going corner.
            // Go corner, opponent doesn't go middle, always win by going middle.
        // Good chances of winning
            // Go middle, if opponent goes corner, go opposing corner.
            // Go corner, if opponent goes middle, go opposing corner.
            // Go edge, opponent plays, go one of the opposing corners.

        /* First we check cases that involve player going middle */
        if (playerArray[1][1]) {
            // player went turn 1 middle
            if(turn == 2) {
                AIrandomPlay(0); // must go corner
                return;
            }
            // player went opposing corner
            else if(turn == 4 && (playerButton.getId().equals("1") || playerButton.getId().equals("3") ||
                                    playerButton.getId().equals("7") || playerButton.getId().equals("9"))) {

                AIrandomPlay(0); // must go corner again
                return;
            }
        }

        /* Then we check cases that involve player going corner */
        if (playerArray[0][0] || playerArray[0][2] || playerArray[2][0] || playerArray[2][2]) {
            // player went turn 1 corner
            if(turn == 2) {
                AIspecificPlay(4); // must go middle
                return;
            }
            // player went double corner
            else if(turn == 4 && ((playerArray[0][0] && playerArray[2][2]) ||
                                   playerArray[0][2] && playerArray[2][0])) {
                AIrandomPlay(1); // must go edge
                return;
            }
        }

        /* Then we check cases that involve player going edge */
        if (playerArray[0][1] || playerArray[1][0] || playerArray[1][2] || playerArray[2][1]) {
            // player went turn one edge or (turn two edge after corner)
            if(turn == 2 || (turn == 4 && !buttons[4].getText().isEmpty())) {
                int playerButtonID = Integer.parseInt( playerButton.getId());

                // If player went the edges on top or bottom
                if (playerButtonID == 2 || playerButtonID == 8) {
                    // Fill its left or right randomly
                    if (random.nextBoolean()) {
                        buttons[playerButtonID - 2].setText(aiMark);
                        fillXOArrays(playerButtonID - 1);
                    } else {
                        buttons[playerButtonID].setText(aiMark);
                        fillXOArrays(playerButtonID + 1);
                    }
                }
                // If player went the edges on left or right
                else if (playerButtonID == 4 || playerButtonID == 6) {
                    // Fill its top or bottom randomly
                    if (random.nextBoolean()) {
                        buttons[playerButtonID - 4].setText(aiMark);
                        fillXOArrays(playerButtonID - 3);
                    } else {
                        buttons[playerButtonID + 2].setText(aiMark);
                        fillXOArrays(playerButtonID + 3);
                    }
                }

                return;
            }
            // player played again and didn't go middle
            else if(turn == 4) {
                AIspecificPlay(4); // must go middle
                return;
            }
        }

        // If none of these cases, playing randomly is sufficient
        AIrandomPlay(2);
    }

    /**
     * Randomly selects one of the buttons based on the input and marks it.
     * @param generateEvenOrOdd If 0, generate an even number btw 0-8 (corners or middle).
     *                          If 1, generate an odd number btw 0-8 (edges).
     *                          Else, generate btw 0-8.
     */
    private void AIrandomPlay(int generateEvenOrOdd) {
        boolean played = false;
        while(!played && turn < 10) {
            int buttonNum = 0;
            if(generateEvenOrOdd == 0) {
                buttonNum = random.nextInt(5) * 2; // 0 2 4 6 8
            } else if(generateEvenOrOdd == 1) {
                buttonNum = random.nextInt(4) * 2 + 1;  // 1 3 5 7
            } else {
                buttonNum = random.nextInt(9); // 0-8
            }
            if(buttons[buttonNum].getText().isEmpty()) {
                buttons[buttonNum].setText( aiMark);
                fillXOArrays(buttonNum + 1);
                played = true;
            }
        }

    }

    /**
     * Marks the button at buttonLoc.
     * @param buttonLoc Button's location in the array (0-8)
     */
    private void AIspecificPlay(int buttonLoc) {
        if(buttons[buttonLoc].getText().isEmpty()) {
            buttons[buttonLoc].setText( aiMark);
            fillXOArrays(buttonLoc + 1);
        }
    }

    /**
     * Fills the arrays for player or AI based on who's turn it is.
     * @param currButtonID Which button to fill (1-9).
     */
    private void fillXOArrays( int currButtonID) {
        int col = (currButtonID - 1) % 3;
        if(isPlayerTurn) { // Fill for player
            if (currButtonID < 4) {
                playerArray[0][col] = true;
            } else if (currButtonID < 7) {
                playerArray[1][col] = true;
            } else {
                playerArray[2][col] = true;
            }
        } else { // Fill for AI
            if (currButtonID < 4) {
                aiArray[0][col] = true;
            } else if (currButtonID < 7) {
                aiArray[1][col] = true;
            } else {
                aiArray[2][col] = true;
            }
        }
    }

    /**
     * Checks if the player won by checking if there are any 3 subsequent player marks.
     * Unnecessary at the moment since the player can't win..
     * @return ShowGameOver() or CheckForTurnsOver()
     */
    private boolean checkForGameOver() {
        // Checks rows and columns
        for (int i = 0; i < 3; i++) {
            if (playerArray[i][0] && playerArray[i][1] && playerArray[i][2]) {
                return showGameOver("You won!");
            } else if (playerArray[0][i] && playerArray[1][i] && playerArray[2][i]) {
                return showGameOver("You won!");
            }
        }
        // Checks crosses
        if (playerArray[0][0] && playerArray[1][1] && playerArray[2][2]) {
            return showGameOver("You won!");
        } else if (playerArray[0][2] && playerArray[1][1] && playerArray[2][0]) {
            return showGameOver("You won!");
        }

        return checkForTurnsOver();
    }

    /**
     * If the turns >= 10, it must be a draw.
     * @return ShowGameOver() or false if turns < 10
     */
    private boolean checkForTurnsOver() {
        if(turn >= 10) {
            if( showGameOver("Draw")) {
                return true;
            }
            Platform.exit();
            System.exit(0);
        }
        return false;
    }

    /**
     * Shows a game over screen with the input text written on it.
     * Prompts if the player wants to play again.
     * @param text The text to show.
     * @return If player wants to keep playing or not.
     */
    private boolean showGameOver( String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("");
        alert.setContentText( text);
        alert.showAndWait();


        Alert alert2 = new Alert(Alert.AlertType.NONE);
        alert2.setTitle("Play Again?");
        alert2.setHeaderText("");
        alert2.setContentText("");
        ButtonType confirmB = new ButtonType("Yes");
        ButtonType cancelB = new ButtonType("No");
        alert2.getButtonTypes().addAll( confirmB, cancelB);
        Optional<ButtonType> optional = alert2.showAndWait();

        if(optional.isPresent() && optional.get() == confirmB) {
            turn = 1;
            initialize();
            return true;
        } else {
            Platform.exit();
            System.exit(0);
        }

        return false;
    }

    /**
     * An alert window that asks who goes first.
     */
    private void promptWhoGoesFirst() {
        ButtonType bt1 = new ButtonType("Me", ButtonBar.ButtonData.OK_DONE);
        ButtonType bt2 = new ButtonType("AI", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.NONE, "Who goes first?", bt1, bt2);
        Optional<ButtonType> result = alert.showAndWait();

        isPlayerTurn = result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE;
        playerWentFirst = isPlayerTurn;
    }

    /**
     * Just fills the buttons array with the 9 buttons and sets their text to null.
     */
    private void setButtons() {
        buttons[0] = button1;
        buttons[1] = button2;
        buttons[2] = button3;
        buttons[3] = button4;
        buttons[4] = button5;
        buttons[5] = button6;
        buttons[6] = button7;
        buttons[7] = button8;
        buttons[8] = button9;
        for(Button b : buttons) {
            b.setText("");
        }
    }

    /**
     * The one who goes first gets the mark "X" and the other gets mark "O".
     * Also initializes their arrays.
     */
    private void setMarksAndArrays() {
        if(playerWentFirst) {
            playerMark = "X";
            aiMark = "O";
            playerArray = Xarray;
            aiArray = Oarray;
        } else {
            playerMark = "O";
            aiMark = "X";
            playerArray = Oarray;
            aiArray = Xarray;
        }
    }

    private void printArray(int which) {
        if (which == 0) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(Xarray[i][j] + " ");
                }
                System.out.println();
            }
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(Oarray[i][j] + " ");
                }
                System.out.println();
            }
        }
    }
}
