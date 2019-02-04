package Dependencies.Games;

import Dependencies.Systems.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Roulette game
 * @Authors Diego Cordova & Nick Chen
 */

public class Roulette extends GamblingGame {
    private int redPot = 0;
    private int blackPot = 0;
    private int greenPot = 0;


    public void placeBet(int bet, User currUser, ){
        currentPlayerBets.replace(currUser, bet);
    }
}
