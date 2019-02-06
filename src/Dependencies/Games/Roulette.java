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
    private int redPot = 0;
    private int blackPot = 0;
    private int greenPot = 0;
    private String[] wheel = new String[15];

    public void placeBet(int betAmount, String bet, User player){
        if(bet.equals("green")){
            greenPot += betAmount;
        }
        if(bet.equals("red")){
            redPot += betAmount;
        }
        if(bet.equals("black")){
            blackPot += betAmount;
        }
    }

    public void generateWheel(String[] wheel){
        for(int i = 0;i<wheel.length;i++){
            wheel[i] = randomColor();
        }
    }

    public String randomColor(){
        int rCounter = 0;
        int bCounter = 0;
        if(bCounter == 7 && rCounter == 7){
            return "green";
        }
        if((int)(Math.random() * 100) > 50){
            rCounter++;
            return "red";
        }else {
            bCounter++;
            return "black";
        }
    }

    public String calculateResult(){

        if(((int)Math.random() *100) <= 55){
            return "black";
        }else if(((int)Math.random() *100) <= 99.7){
            return "red";
        }else
            return "green";

    }




}
