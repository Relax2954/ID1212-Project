/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.controller;

import java.nio.ByteBuffer;
import server.model.GameLogicC;
import server.model.Scoring;

/**
 *
 * @author Relax2954
 */
public class Controller {

    public volatile String gamelogcheckerTable;
    GameLogicC gamelogiccc;
    public volatile int score1 = 0;
    public volatile int score2 = 0;
    ByteBuffer BufferedTempor;
    private volatile Scoring myscore1;
    private volatile Scoring myscore2;

    public Controller() {
        myscore1 = new Scoring();
        myscore2 = new Scoring();
        gamelogiccc = new GameLogicC();
        gamelogcheckerTable = gamelogiccc.checkerTableString;
    }

    public Character getWinner() {
        return gamelogiccc.getWinner();
    }

    public int isFinished() {
        return gamelogiccc.isFinished();
    }
    public void res(){
        gamelogiccc = new GameLogicC();
        gamelogcheckerTable = gamelogiccc.checkerTableString;
    }

    public String MidAction(int inputInt) {
        gamelogiccc.gamelogic(inputInt);
        return gamelogiccc.checkerTableString;
    }

    public boolean isWon() {
        return gamelogiccc.isWon();
    }

    public char getCurrentPick() {
        return gamelogiccc.getCurrentPick();
    }

    public void scoreIncrement() {
        if (gamelogiccc.getWinner() == 'X') {
            score1 = myscore1.scoreincrement1();
        } else {
            score2 = myscore2.scoreincrement2();
        }
    }
}
