package Dependencies.Games;

import Dependencies.Systems.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Roulette game
 * @Authors Diego Cordova & Nick Chen
 */


//change place bet there is no pot just multiplies your bet if you win or just get nothing

public class Roulette extends GamblingGame {

    public void placeBet(int betAmount, User player){
        userManager.updatePlayerBalance(player, -(betAmount));
    }

    public String[] generateWheel(){
        String[] wheel = new String[15];
        wheel[0] = "green";
        for(int i = 1;i<wheel.length;i++){
            if(i >8) {
                wheel[i] = "black";
            }else {
                wheel[i] = "red";
            }
        }
        return wheel;
    }

    public String calculateResult(){

        if(((int)Math.random() *100) <= 55){
            return "black";
        }else if(((int)Math.random() *100) <= 99){
            return "red";
        }else
            return "green";

    }

    public void updateWinnerBalance(String winningColor,User player, int winAMount){
            userManager.updatePlayerBalance(player, winAMount);

    }




}
