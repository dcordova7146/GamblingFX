package Controllers;

import Dependencies.Games.Crash;
import Dependencies.Systems.User;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import sun.rmi.runtime.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for the Crash Game Scene.
 * TO DO - Multi Colored Circles (DESIGN)
 * @Authors Afaq Anwar & Wai Hin Leung
 * @Version 02/07/2019
 */
public class CrashGameController {
    @FXML private Pane pane;
    @FXML private Circle circle;
    @FXML private VBox playerPane;
    @FXML private JFXTextField amountField;
    @FXML private JFXButton playButton;
    @FXML private JFXButton clearButton;
    @FXML private StackPane stackPane;
    @FXML private Label timer;
    @FXML private Label textMultiplier;
    private boolean dialogOpen;

    private Crash crashGame;

    /**
     * Runs on Scene load.
     */
    public void initialize() {
        crashGame = new Crash(LoginController.currentUser);
        this.startPregameTimer();
        dialogOpen = false;
    }

    /**
     * Initializes the 10 second timer before the Crash game starts.
     */
    private void startPregameTimer() {
        Timer pregameTimer = new Timer();
        TimerTask countdown = new TimerTask() {
            int currSeconds = 15;
            @Override
            public void run() {
                timer.setVisible(true);
                if (!crashGame.isGameRunning()) {
                    Platform.runLater(() -> resetUI());
                    Platform.runLater(() -> resetControls());
                    crashGame = new Crash(LoginController.currentUser);
                }
                if (currSeconds > 0) {
                    Platform.runLater(() -> timer.setText("Starting in: " + currSeconds + " Seconds"));
                    currSeconds--;
                } else {
                    Platform.runLater(() -> disableControls(false));
                    Platform.runLater(() -> timer.setVisible(false));
                    Platform.runLater(() -> disableFunctionalControls(true));
                    Platform.runLater(() -> disableAllControlsOnIdle());
                    Platform.runLater(() -> animateGame());
                    this.cancel();
                }
            }
        };
        pregameTimer.schedule(countdown, 1000, 1000);
    }

    /**
     * Animates the game using critical functions.
     */
    private void animateGame() {
        AnimationTimer gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (crashGame.isGameRunning()) {
                    crashGame.setCurrentMultiplier(crashGame.getCurrentMultiplier() + ((crashGame.getCurrentMultiplier() / 5) * 0.03));
                    crashGame.updatePlayerMultipliers();
                    textMultiplier.setText(((double) Math.round(crashGame.getCurrentMultiplier() * 100) / 100) + "x");
                    circle.setRadius(circle.getRadius() + ((crashGame.getCurrentMultiplier() / 5) * 0.03));
                    updatePlayerList();
                } else {
                    crashGame.setLosingPlayers();
                    if (crashGame.userManager.getStatusOfUser(LoginController.currentUser).equals("Lost")) {
                        displayLosingAlert();
                    }
                    updatePlayerList();
                    Platform.runLater(() -> startPregameTimer());
                    this.stop();
                }
            }
        };
        gameTimer.start();
    }

    /**
     * Button Handler.
     * @param actionEvent Any ActionEvent.
     */
    @FXML
    private void handleButtons(ActionEvent actionEvent) {
        if (actionEvent.getSource() == playButton && crashGame.userManager.getStatusOfUser(LoginController.currentUser).equals("Playing") && crashGame.isGameRunning()) {
            crashGame.userManager.setStatusOfUser(LoginController.currentUser, "Won");
            this.displayWinningAlert();
            crashGame.updatePlayersBalance();
            this.disableControls(true);
            playButton.setText("Play");
        } else if (actionEvent.getSource() == playButton) {
            if (validateBet()) {
                crashGame.userManager.setStatusOfUser(LoginController.currentUser, "Playing");
                crashGame.placeBet(Integer.parseInt(amountField.getText()), LoginController.currentUser);
                playButton.setText("Withdraw");
                this.updatePlayerList();
                this.disableControls(true);
            } else {
                this.displayAlert();
            }
        }
        if (actionEvent.getSource() == clearButton) {
            amountField.clear();
        }
    }

    /**
     * Makes sure the betting amount entered is a whole number that is less than 1,000,000,000.
     * @return True if it meets the requirements, false otherwise.
     */
    private boolean validateBet() {
      return (amountField.getText().length() < 6 && amountField.getText().length() > 0 && crashGame.userManager.validateAmount(LoginController.currentUser, Integer.parseInt(amountField.getText())));
    }

    /**
     * Displays alerts based on the user error.
     */
    private void displayAlert() {
        JFXDialog dialog = new JFXDialog();
        if (!dialogOpen) {
            if (amountField.getText().matches("[^0-9]")) {
                dialog = new JFXDialog();
                dialog.setContent(new Label("Bets can be whole numbers only!"));
                dialogOpen = true;
            } else if (amountField.getText().length() > 6) {
                dialog = new JFXDialog();
                dialog.setContent(new Label("Cannot place a bet over 100,000!"));
                dialogOpen = true;
            } else if (amountField.getText().length() == 0) {
                dialog = new JFXDialog();
                dialog.setContent(new Label("Please place a valid bet amount."));
                dialogOpen = true;
            } else if (crashGame.userManager.validateAmount(LoginController.currentUser, Integer.parseInt(amountField.getText()))) {
                dialog = new JFXDialog();
                dialog.setContent(new Label("Insufficient Funds!"));
                dialogOpen = true;
            }
            final JFXDialog finalDialog = dialog;
            finalDialog.show(stackPane);
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                finalDialog.close();
                dialogOpen = false;
            });
            pause.play();
        }
    }

    /**
     * Displays a "winning" JFXDialog if the User has won the game.
     */
    private void displayWinningAlert() {
        JFXDialog dialog = new JFXDialog();
        dialog.setContent(new Label("You won " + Math.floor(crashGame.getPlayerBet(LoginController.currentUser) * crashGame.getPlayerMultiplier(LoginController.currentUser)) + " Christ Coins!"));
        dialog.show(stackPane);
        dialogOpen = true;
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            dialog.close();
            dialogOpen = false;
        });
        pause.play();
    }

    /**
     * Displays a "losing" JFXDialog if the User has lost the game.
     */
    private void displayLosingAlert() {
        JFXDialog dialog = new JFXDialog();
        dialog.setContent(new Label("Better luck next time!"));
        dialog.show(stackPane);
        dialogOpen = true;
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(event -> {
            dialog.close();
            dialogOpen = false;
        });
        pause.play();
    }

    /**
     * Disables amount field, and clear button.
     * @param bool
     */
    private void disableFunctionalControls(boolean bool) {
        amountField.setDisable(true);
        clearButton.setDisable(bool);
    }

    /**
     * Disables controls.
     * @param bool True if all controls are to be disabled, false otherwise.
     */
    private void disableControls(boolean bool) {
        playButton.setDisable(bool);
        clearButton.setDisable(bool);
        amountField.setDisable(bool);
    }

    /**
     * Resets all controls back to a default state.
     */
    private void resetControls() {
        playButton.setText("Play");
        this.disableControls(false);
    }

    /**
     * If the player is IDLE, all controls are disabled.
     */
    private void disableAllControlsOnIdle() {
        if (!crashGame.userManager.getStatusOfUsers().containsKey(LoginController.currentUser)) {
            this.disableControls(true);
        }
    }

    /**
     * Updates the List of Players with Labels inside of the playerPane VBOX.
     */
    private void updatePlayerList() {
        playerPane.getChildren().clear();
        for (User user : crashGame.userManager.getCurrentActiveUsers()) {
            Label userLabel = new Label();
            if (!crashGame.userManager.getStatusOfUsers().containsKey(user)) {
                userLabel.setText(user.getFirstName().toUpperCase() + " [IDLE]");
            } else if (crashGame.userManager.getStatusOfUser(user).equals("Lost")) {
               userLabel.setText(user.getFirstName().toUpperCase() + " [" + crashGame.userManager.getStatusOfUser(user) + "] BET: " + crashGame.getPlayerBet(user) + " @ X.XX");
            } else {
                userLabel.setText(user.getFirstName().toUpperCase() + " [" + crashGame.userManager.getStatusOfUser(user) + "] BET: " + crashGame.getPlayerBet(user) + " @ " + (double) Math.round(crashGame.getPlayerMultiplier(user) * 100) / 100 + "x");
            }
            playerPane.getChildren().add(userLabel);
        }
    }

    /**
     * Resets the UI for game restart.
     */
    private void resetUI() {
        circle.setRadius(10);
        textMultiplier.setText("1.00x");
    }
}