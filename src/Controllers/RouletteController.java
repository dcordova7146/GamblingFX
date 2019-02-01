package Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Label;

public class RouletteController {
    public Label user_name;
    public Label user_balance;
    public Label message;
    public JFXTextField bet_amount;
    public JFXButton lock_in;
    public JFXButton clear;

    public void initialize(){
        user_name.setText("Welcome, " + LoginController.currentUser.getFirstName() + ".");
        user_balance.setText("You currently have " + LoginController.currentUser.getBalance() + " coin(s).");
        message.setText("How much would you like to bet?");
    }
}
