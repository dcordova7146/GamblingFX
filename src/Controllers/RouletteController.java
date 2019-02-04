package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class RouletteController {
    public Pane pane;
    public Label user_name;
    public Label user_balance;
    public Label message1;
    public JFXTextField bet_amount;
    public JFXButton lock_in;
    public JFXButton clear;

    public Label message2;
    public Label message3;
    public JFXButton red;
    public JFXButton black;
    public JFXButton green;

    public StackPane stackPane;

    public int multiplier;

    public void initialize(){
        user_name.setText("Welcome, " + LoginController.currentUser.getFirstName() + ".");
        user_balance.setText("You currently have " + LoginController.currentUser.getBalance() + " coin(s).");
        message1.setText("How much would you like to bet?");

        message2.setText("Please choose a color.");
    }

    public void red(ActionEvent actionEvent){
        multiplier = 2;
        message3.setText("You have selected red. The multiplier is " + multiplier + ".");
    }

    public void black(ActionEvent actionEvent){
        multiplier = 2;
        message3.setText("You have selected black. The multiplier is " + multiplier + ".");
    }

    public void green(ActionEvent actionEvent){
        multiplier = 14;
        message3.setText("You have selected green. The multiplier is " + multiplier + ".");
    }

    /**
     * Makes sure the betting amount entered is a hole number that is less than 1,000,000,000.
     * @return True if it meets the requirements, false otherwise.
     */
    private boolean validateAmount() {
        return (bet_amount.getText().matches("[0-9]") && bet_amount.getText().length() < 10);
    }

    /**
     * Displays alerts based on the user error.
     */
    private void displayAlert() {
        if (bet_amount.getText().matches("[^0-9]")) {
            JFXDialog dialog = new JFXDialog();
            dialog.setContent(new Label("Bets can be whole numbers only!"));
            dialog.show(stackPane);
        } else if (bet_amount.getText().length() > 10) {
            JFXDialog dialog = new JFXDialog();
            dialog.setContent(new Label("Cannot place a bet over 1,000,000,000!"));
            dialog.show(stackPane);
        }
    }
}
